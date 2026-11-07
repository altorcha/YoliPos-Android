package com.example.yolipos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yolipos.data.local.entities.ProductoEntidad
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM tabla_productos ORDER BY nombreProducto ASC")
    fun obtenerTodosLosProductos(): Flow<List<ProductoEntidad>>

    @Query("SELECT * FROM tabla_productos WHERE categoriaProducto = :categoriaBuscada AND estaDisponible = 1 ORDER BY nombreProducto ASC")
    fun obtenerProductosPorCategoria(categoriaBuscada: String): Flow<List<ProductoEntidad>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(productoEntidad: ProductoEntidad): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarListaDeProductos(listaDeProductos: List<ProductoEntidad>)

    @Update
    suspend fun actualizarProducto(productoEntidad: ProductoEntidad)

    @Delete
    suspend fun eliminarProducto(productoEntidad: ProductoEntidad)
}