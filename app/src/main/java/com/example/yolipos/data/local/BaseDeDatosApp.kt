package com.example.yolipos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.yolipos.data.local.dao.ComandaDao
import com.example.yolipos.data.local.dao.ProductoDao
import com.example.yolipos.data.local.entities.ComandaEntidad
import com.example.yolipos.data.local.entities.ProductoEntidad

//BD principal de la app mediante la room
@Database(
    entities = [ComandaEntidad::class, ProductoEntidad::class],
    version = 1,
    exportSchema = false
)
abstract class BaseDeDatosApp : RoomDatabase() {

    abstract fun obtenerComandaDao(): ComandaDao
    abstract fun obtenerProductoDao(): ProductoDao

    companion object {
        @Volatile
        private var INSTANCIA_BASE_DE_DATOS: BaseDeDatosApp? = null

        fun obtenerBaseDeDatos(contextoAplicacion: Context): BaseDeDatosApp {
            return INSTANCIA_BASE_DE_DATOS ?: synchronized(this) {
                val nuevaInstanciaCreada = Room.databaseBuilder(
                    contextoAplicacion.applicationContext,
                    BaseDeDatosApp::class.java,
                    "base_de_datos_yolipos.db"
                ).build()
                INSTANCIA_BASE_DE_DATOS = nuevaInstanciaCreada
                nuevaInstanciaCreada
            }
        }
    }
}