package com.example.yolipos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//tabla de comandas
@Entity(tableName = "tabla_comandas")
data class ComandaEntidad(
    @PrimaryKey(autoGenerate = true)
    val identificadorComanda: Long = 0,
    val nombreCliente: String,
    val origenPedido: String,
    val estadoComanda: String,
    val metodoDePago: String,
    val montoTotal: Double,
    val fechaHoraRegistro: Long,
    val detallesEspecificos: String
)