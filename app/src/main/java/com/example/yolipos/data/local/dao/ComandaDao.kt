package com.example.yolipos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yolipos.data.local.entities.ComandaEntidad
import kotlinx.coroutines.flow.Flow

//DAO para los cruds y consultas de filtros en la tabla de comandas
@Dao
interface ComandaDao{
    @Query("SELECT * FROM  tabla_comandas ORDER by fechaHoraRegistro DESC")
    fun obtenerTodasLasComandas(): Flow<List<ComandaEntidad>>

    @Query("SELECT * FROM tabla_comandas WHERE estadoComanda = :estadoBuscado ORDER BY fechaHoraRegistro ASC")
    fun obtenerComandasPorEstado(estadoBuscado: String): Flow<List<ComandaEntidad>>

    @Query("SELECT * FROM tabla_comandas WHERE fechaHoraRegistro BETWEEN :fechaInicio AND :fechaFin ORDER BY fechaHoraRegistro ASC")
    fun obtenerComandasPorRangoDeFechas(fechaInicio: Long, fechaFin: Long): Flow<List<ComandaEntidad>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarComanda(comandaEntidad: ComandaEntidad): Long

    @Update
    suspend fun actualizarComanda(comandaEntidad: ComandaEntidad)

    @Query("UPDATE tabla_comandas SET estadoComanda = :nuevoEstado WHERE identificadorComanda = :identificadorComanda")
    suspend fun actualizarEstadoComanda(identificadorComanda: Long, nuevoEstado: String)

    @Delete
    suspend fun eliminarComanda(comandaEntidad: ComandaEntidad)
}
