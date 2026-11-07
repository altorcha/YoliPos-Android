package com.example.yolipos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yolipos.data.local.GestorPreferencias
import com.example.yolipos.domain.RepositorioYoliPOS
import com.example.yolipos.model.Comanda
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//view model que maneja lo de la caja registradora, filtro de comandas por fechas y los turnos
class CierreDeCajaViewModel(
    private val repositorioYoliPOS: RepositorioYoliPOS,
    private val gestorPreferencias: GestorPreferencias
) : ViewModel() {

    // comandas dentro del rango de fecha seleccionado
    private val _listaDeComandasEnRango = MutableStateFlow<List<Comanda>>(emptyList())
    val listaDeComandasEnRango: StateFlow<List<Comanda>> = _listaDeComandasEnRango.asStateFlow()

    // Marca temporal de fecha de inicio para el filtro (milisegundos)
    private val _fechaInicioMilisegundos = mutableStateOf(System.currentTimeMillis() - (86400000L * 7)) // Hace 7 días por defecto
    val fechaInicioMilisegundos: State<Long> = _fechaInicioMilisegundos

    // Marca temporal de fecha de fin para el filtro (milisegundos)
    private val _fechaFinMilisegundos = mutableStateOf(System.currentTimeMillis())
    val fechaFinMilisegundos: State<Long> = _fechaFinMilisegundos

    // Totales calculados para la caja
    private val _totalEfectivoCalculado = mutableStateOf(0.0)
    val totalEfectivoCalculado: State<Double> = _totalEfectivoCalculado

    private val _totalTransferenciaCalculado = mutableStateOf(0.0)
    val totalTransferenciaCalculado: State<Double> = _totalTransferenciaCalculado

    private val _totalVentasGlobalCalculado = mutableStateOf(0.0)
    val totalVentasGlobalCalculado: State<Double> = _totalVentasGlobalCalculado

    // Datos del turno activo recabados desde las preferencias
    private val _nombreCajeroActual = mutableStateOf("")
    val nombreCajeroActual: State<String> = _nombreCajeroActual

    private val _cajaAbiertaActualmente = mutableStateOf(false)
    val cajaAbiertaActualmente: State<Boolean> = _cajaAbiertaActualmente

    private val _horaAperturaTurno = mutableStateOf("")
    val horaAperturaTurno: State<String> = _horaAperturaTurno

    init {
        cargarPreferenciasDelTurno()
        consultarComandasPorRangoDeFechas()
    }

    //carga los turnos del cajero
    fun cargarPreferenciasDelTurno() {
        _nombreCajeroActual.value = gestorPreferencias.obtenerNombreCajero()
        _cajaAbiertaActualmente.value = gestorPreferencias.obtenerEstadoCaja()
        _horaAperturaTurno.value = gestorPreferencias.obtenerHoraApertura()
    }

    //registrar turno del cajero
    fun abrirTurnoDeCaja(nombreDelCajero: String, horaActualTexto: String) {
        gestorPreferencias.guardarConfiguracionTurno(
            nombreCajero = nombreDelCajero,
            cajaAbierta = true,
            temaOscuroActivado = gestorPreferencias.obtenerEstadoTemaOscuro(),
            horaApertura = horaActualTexto
        )
        cargarPreferenciasDelTurno()
    }

    //finaliza el turno del cajero
    fun cerrarTurnoDeCaja() {
        gestorPreferencias.guardarConfiguracionTurno(
            nombreCajero = _nombreCajeroActual.value,
            cajaAbierta = false,
            temaOscuroActivado = gestorPreferencias.obtenerEstadoTemaOscuro(),
            horaApertura = _horaAperturaTurno.value
        )
        cargarPreferenciasDelTurno()
    }

    fun actualizarRangoDeFechas(nuevaFechaInicio: Long, nuevaFechaFin: Long) {
        _fechaInicioMilisegundos.value = nuevaFechaInicio
        _fechaFinMilisegundos.value = nuevaFechaFin
        consultarComandasPorRangoDeFechas()
    }

    //consultar comandas por fechas
    fun consultarComandasPorRangoDeFechas() {
        viewModelScope.launch {
            repositorioYoliPOS.obtenerComandasPorRangoDeFechas(
                fechaInicio = _fechaInicioMilisegundos.value,
                fechaFin = _fechaFinMilisegundos.value
            ).collect { listaDeComandasObtenida ->
                _listaDeComandasEnRango.value = listaDeComandasObtenida
                calcularTotalesDeVenta(listaDeComandasObtenida)
            }
        }
    }

    //calcular totales de venta
    private fun calcularTotalesDeVenta(listaDeComandas: List<Comanda>) {
        var acumuladoEfectivo = 0.0
        var acumuladoTransferencia = 0.0

        for (comandaActual in listaDeComandas) {
            if (comandaActual.metodoDePago == "Efectivo") {
                acumuladoEfectivo += comandaActual.montoTotal
            } else if (comandaActual.metodoDePago == "Transferencia") {
                acumuladoTransferencia += comandaActual.montoTotal
            }
        }

        _totalEfectivoCalculado.value = acumuladoEfectivo
        _totalTransferenciaCalculado.value = acumuladoTransferencia
        _totalVentasGlobalCalculado.value = acumuladoEfectivo + acumuladoTransferencia
    }


    fun formatearMilisegundosAFechaTexto(milisegundos: Long): String {
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatoFecha.format(Date(milisegundos))
    }
}