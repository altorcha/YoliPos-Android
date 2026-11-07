package com.example.yolipos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_comandas")
data class ComandaEntidad(
    @PrimaryKey(autoGenerate = true)
    val identificadorComando: Long = 0,
    val nombreCliente: String,
    val origenPedido: String,
    val estadoComando: String,
    val metodoDePago: String,
    val montoTotal: Double,
    val fechaHoraRegistro: Long,
    val detallesEspecificos: String
)