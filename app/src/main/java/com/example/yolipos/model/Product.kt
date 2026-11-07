package com.example.yolipos.model

data class Producto(
    val identificadorProducto: Long = 0,
    val nombreProducto: String,
    val precioProducto: Double,
    val categoriaProducto: String,
    val estaDisponible: Boolean = true,
    val rutaImagenProducto: String = ""
)