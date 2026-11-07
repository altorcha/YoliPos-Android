package com.example.yolipos.domain

import com.example.yolipos.data.local.dao.ComandaDao
import com.example.yolipos.data.local.dao.ProductoDao
import com.example.yolipos.data.local.entities.ComandaEntidad
import com.example.yolipos.data.local.entities.ProductoEntidad
import com.example.yolipos.model.Comanda
import com.example.yolipos.model.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//repositorio que abstrae el origen de los datos locales y remoto
class RepositorioYoliPOS(
    private val objetoComandaDao: ComandaDao,
    private val objetoProductoDao: ProductoDao
){
    fun obtenerTodasLasComandas(): Flow <List<Comanda>>{
        return objetoComandaDao.obtenerTodasLasComandas().map { listaEntidadesComanda ->
            listaEntidadesComanda.map { entidadComanda ->
                convertirEntidadAModeloComanda(entidadComanda)
            }
        }
    }
    //comamdas por status
    fun obtenerComandasPorEstado(estadoBuscado: String): Flow<List<Comanda>> {
        return objetoComandaDao.obtenerComandasPorEstado(estadoBuscado).map { listaEntidadesComanda ->
            listaEntidadesComanda.map { entidadComanda ->
                convertirEntidadAModeloComanda(entidadComanda)
            }
        }
    }

    //comandas por fechas
    fun obtenerComandasPorRangoDeFechas(fechaInicio: Long, fechaFin: Long): Flow<List<Comanda>> {
        return objetoComandaDao.obtenerComandasPorRangoDeFechas(fechaInicio, fechaFin).map { listaEntidadesComanda ->
            listaEntidadesComanda.map { entidadComanda ->
                convertirEntidadAModeloComanda(entidadComanda)
            }
        }
    }
    //agregar comanda
    suspend fun insertarComanda(comanda: Comanda): Long {
        val entidadComanda = convertirModeloAEntidadComanda(comanda)
        return objetoComandaDao.insertarComanda(entidadComanda)
    }
    //actualizar status de comanda
    suspend fun actualizarEstadoComanda(identificadorComanda: Long, nuevoEstado: String) {
        objetoComandaDao.actualizarEstadoComanda(identificadorComanda, nuevoEstado)
    }

    //quitar comanda
    suspend fun eliminarComanda(comanda: Comanda) {
        val entidadComanda = convertirModeloAEntidadComanda(comanda)
        objetoComandaDao.eliminarComanda(entidadComanda)
    }

    //listado de productos
    fun obtenerTodosLosProductos(): Flow<List<Producto>> {
        return objetoProductoDao.obtenerTodosLosProductos().map { listaEntidadesProducto ->
            listaEntidadesProducto.map { entidadProducto ->
                convertirEntidadAModeloProducto(entidadProducto)
            }
        }
    }

    //listado de productos por categoria
    fun obtenerProductosPorCategoria(categoriaBuscada: String): Flow<List<Producto>> {
        return objetoProductoDao.obtenerProductosPorCategoria(categoriaBuscada).map { listaEntidadesProducto ->
            listaEntidadesProducto.map { entidadProducto ->
                convertirEntidadAModeloProducto(entidadProducto)
            }
        }
    }

    //agregar producto
    suspend fun insertarListaDeProductos(listaDeProductos: List<Producto>) {
        val listaEntidadesProducto = listaDeProductos.map { productoModelo ->
            convertirModeloAEntidadProducto(productoModelo)
        }
        objetoProductoDao.insertarListaDeProductos(listaEntidadesProducto)
    }

    //mapeo de funciones
    private fun convertirEntidadAModeloComanda(entidadComanda: ComandaEntidad): Comanda {
        return Comanda(
            identificadorComanda = entidadComanda.identificadorComanda,
            nombreCliente = entidadComanda.nombreCliente,
            origenPedido = entidadComanda.origenPedido,
            estadoComanda = entidadComanda.estadoComanda,
            metodoDePago = entidadComanda.metodoDePago,
            montoTotal = entidadComanda.montoTotal,
            fechaHoraRegistro = entidadComanda.fechaHoraRegistro,
            detallesEspecificos = entidadComanda.detallesEspecificos
        )
    }

    private fun convertirModeloAEntidadComanda(modeloComanda: Comanda): ComandaEntidad {
        return ComandaEntidad(
            identificadorComanda = modeloComanda.identificadorComanda,
            nombreCliente = modeloComanda.nombreCliente,
            origenPedido = modeloComanda.origenPedido,
            estadoComanda = modeloComanda.estadoComanda,
            metodoDePago = modeloComanda.metodoDePago,
            montoTotal = modeloComanda.montoTotal,
            fechaHoraRegistro = modeloComanda.fechaHoraRegistro,
            detallesEspecificos = modeloComanda.detallesEspecificos
        )
    }

    private fun convertirEntidadAModeloProducto(entidadProducto: ProductoEntidad): Producto {
        return Producto(
            identificadorProducto = entidadProducto.identificadorProducto,
            nombreProducto = entidadProducto.nombreProducto,
            precioProducto = entidadProducto.precioProducto,
            categoriaProducto = entidadProducto.categoriaProducto,
            estaDisponible = entidadProducto.estaDisponible,
            rutaImagenProducto = entidadProducto.rutaImagenProducto
        )
    }

    private fun convertirModeloAEntidadProducto(modeloProducto: Producto): ProductoEntidad {
        return ProductoEntidad(
            identificadorProducto = modeloProducto.identificadorProducto,
            nombreProducto = modeloProducto.nombreProducto,
            precioProducto = modeloProducto.precioProducto,
            categoriaProducto = modeloProducto.categoriaProducto,
            estaDisponible = modeloProducto.estaDisponible,
            rutaImagenProducto = modeloProducto.rutaImagenProducto
        )
    }
}