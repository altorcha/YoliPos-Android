package com.example.yolipos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yolipos.domain.RepositorioYoliPOS
import com.example.yolipos.model.Comanda
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//viewmodel que gestiona el estado de la interfaz  del tablero de comandas
//permite filtrar por estados y actualizar pedidos
class ComandasViewModel(
    private val repositorioYoliPOS: RepositorioYoliPOS
): ViewModel(){

    //mostrar la lista de comandas en la IU
    private val _listaDeComandas = MutableStateFlow<List<Comanda>>(emptyList())
    val listaDeComandas: StateFlow<List<Comanda>> = _listaDeComandas.asStateFlow()

    //controlar el filtro del status de las comandas
    private val _filtroEstadoSeleccionado = mutableStateOf("Todos")
    val filtroEstadoSeleccionado: State<String> = _filtroEstadoSeleccionado

    //mostrar animacion de carga en la pantalla
    private val _estaCargandoInformacion = mutableStateOf(false)
    val estaCargandoInformacion: State<Boolean> = _estaCargandoInformacion

    init {
        cargarComandasSegunFiltro()
    }
    //cargar y escuchar las comandaas
    fun cargarComandasSegunFiltro(){
        viewModelScope.launch {
            _estaCargandoInformacion.value = true
            val estadoActualFiltro = _filtroEstadoSeleccionado.value

            val flujoComandas = if (estadoActualFiltro == "Todos"){
                repositorioYoliPOS.obtenerTodasLasComandas()
            }else{
                repositorioYoliPOS.obtenerComandasPorEstado(estadoActualFiltro)
            }
            flujoComandas.collect { listaDeComandasObtenida ->
                _listaDeComandas.value = listaDeComandasObtenida
                _estaCargandoInformacion.value = false
            }
        }
    }
    //cambiar el filtro de estado
    fun cambiarFiltroDeEstado(nuevoEstadoFiltro: String) {
        _filtroEstadoSeleccionado.value = nuevoEstadoFiltro
        cargarComandasSegunFiltro()
    }
    //avanza o modifica el estado deuna comanda
    fun cambiarEstadoDeComanda(identificadorComanda: Long, nuevoEstadoOperativo: String) {
        viewModelScope.launch {
            repositorioYoliPOS.actualizarEstadoComanda(identificadorComanda, nuevoEstadoOperativo)
        }
    }
    //registra una comanda
    fun registrarNuevaComanda(nuevaComanda: Comanda) {
        viewModelScope.launch {
            repositorioYoliPOS.insertarComanda(nuevaComanda)
        }
    }
    //elimina una comanda
    fun eliminarComandaExistente(comandaAEliminar: Comanda) {
        viewModelScope.launch {
            repositorioYoliPOS.eliminarComanda(comandaAEliminar)
        }
    }
}