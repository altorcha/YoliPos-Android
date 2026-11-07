package com.example.yolipos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//tabla de productos
@Entity(tableName = "tabla_productos")
data class ProductoEntidad(
    @PrimaryKey(autoGenerate = true)
    val identificadorProducto: Long = 0,
    val nombreProducto: String,
    val precioProducto: Double,
    val categoriaProducto: String,
    val estaDisponible: Boolean,
    val rutaImagenProducto: String
)