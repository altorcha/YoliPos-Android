package com.example.yolipos.model

//esto representa una comanda registrada
data class Comanda(
    val identificadorComanda: Long = 0,
    val nombreCliente: String,
    val origenPedido: String,
    val estadoComanda: String,
    val metodoDePago: String,
    val montoTotal: Double,
    val fechaHoraRegistro: Long = System.currentTimeMillis(),
    val detallesEspecificos: String = ""
)