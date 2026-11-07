package com.example.yolipos.data.local

import android.content.Context
import android.content.SharedPreferences

//maneja las configuraciones de turno, datos del cajero y la interfaz (preferencas
class GestorPreferencias(contexto: Context){
    private val preferenciasCompartidas: SharedPreferences =
        contexto.getSharedPreferences("PreferenciasUsuarioYoliPOS", Context.MODE_PRIVATE)
    companion object{
        private const val CLAVE_NOMBRE_CAJERO = "clave_nombre_cajero"
        private const val CLAVE_CAJA_ABIERTA = "clave_caja_abierta"
        private const val CLAVE_TEMA_OSCURO = "clave_tema_oscuro"
        private const val CLAVE_HORA_APERTURA = "clave_hora_apertura"
    }
    fun guardarConfiguracionTurno(
        nombreCajero: String,
        cajaAbierta: Boolean,
        temaOscuroActivado: Boolean,
        horaApertura: String
    ){
        val editorPreferencias = preferenciasCompartidas.edit()
        editorPreferencias.putString(CLAVE_NOMBRE_CAJERO, nombreCajero)
        editorPreferencias.putBoolean(CLAVE_CAJA_ABIERTA, cajaAbierta)
        editorPreferencias.putBoolean(CLAVE_TEMA_OSCURO, temaOscuroActivado)
        editorPreferencias.putString(CLAVE_HORA_APERTURA, horaApertura)
        editorPreferencias.apply()
    }
    fun obtenerNombreCajero(): String {
        val nombreRegistrado = preferenciasCompartidas.getString(CLAVE_NOMBRE_CAJERO, "")
        return nombreRegistrado ?: ""
    }
    fun obtenerEstadoCaja(): Boolean {
        return preferenciasCompartidas.getBoolean(CLAVE_CAJA_ABIERTA, false)
    }
    fun obtenerEstadoTemaOscuro(): Boolean {
        return preferenciasCompartidas.getBoolean(CLAVE_TEMA_OSCURO, false)
    }
    fun obtenerHoraApertura(): String {
        val horaRegistrada = preferenciasCompartidas.getString(CLAVE_HORA_APERTURA, "")
        return horaRegistrada ?: ""
    }
    fun limpiarPreferenciasTurno() {
        preferenciasCompartidas.edit().clear().apply()
    }
}