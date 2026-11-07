package com.example.yolipos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yolipos.domain.RepositorioYoliPOS
import com.example.yolipos.model.Comanda
import com.example.yolipos.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//clase que es como un carrito, que tiene el prodcuto y las catnidades
data class ElementoPedidoActual(
    val producto: Producto,
    val cantidadSolicitada: Int = 1,
    val modificadoresTexto: String = ""
)

//viewmodel que es la logica del punto de venta
//el catalogo de productos, el carrito de compras y la creacion de comanda
class PuntoDeVentaViewModel(
    private val repositorioYoliPOS: RepositorioYoliPOS
): ViewModel() {
    // Lista completa de productos disponibles obtenida del catálogo
    private val _listaDeProductosDisponibles = MutableStateFlow<List<Producto>>(emptyList())
    val listaDeProductosDisponibles: StateFlow<List<Producto>> = _listaDeProductosDisponibles.asStateFlow()

    // Categoría seleccionada para filtrar en la cuadrícula de mostrador (ej. "Todas", "Tortas", "Bebidas")
    private val _categoriaSeleccionada = mutableStateOf("Todas")
    val categoriaSeleccionada: State<String> = _categoriaSeleccionada

    // Lista de elementos añadidos a la comanda actual en preparación
    private val _listaDeElementosEnPedido = MutableStateFlow<List<ElementoPedidoActual>>(emptyList())
    val listaDeElementosEnPedido: StateFlow<List<ElementoPedidoActual>> = _listaDeElementosEnPedido.asStateFlow()

    // Nombre del cliente para la orden actual
    private val _nombreDelClienteInput = mutableStateOf("")
    val nombreDelClienteInput: State<String> = _nombreDelClienteInput

    // Origen seleccionado para la orden ("Mostrador" o "WhatsApp")
    private val _origenDelPedidoSeleccionado = mutableStateOf("Mostrador")
    val origenDelPedidoSeleccionado: State<String> = _origenDelPedidoSeleccionado

    // Metodo de pago seleccionado ("Efectivo" o "Transferencia")
    private val _metodoDePagoSeleccionado = mutableStateOf("Efectivo")
    val metodoDePagoSeleccionado: State<String> = _metodoDePagoSeleccionado

    init {
        cargarCatalogoDeProductos()
    }
    //cargar el catalogo
    fun cargarCatalogoDeProductos() {
        viewModelScope.launch {
            val categoriaActual = _categoriaSeleccionada.value
            val flujoProductos = if (categoriaActual == "Todas") {
                repositorioYoliPOS.obtenerTodosLosProductos()
            } else {
                repositorioYoliPOS.obtenerProductosPorCategoria(categoriaActual)
            }

            flujoProductos.collect { listaObtenida ->
                _listaDeProductosDisponibles.value = listaObtenida
            }
        }
    }
    //cambiar la categoria de productos
    fun cambiarCategoriaSeleccionada(nuevaCategoria: String) {
        _categoriaSeleccionada.value = nuevaCategoria
        cargarCatalogoDeProductos()
    }
    //agregar un producto a un pedido o agregar cantidad extra
    fun agregarProductoAPedido(productoSeleccionado: Producto) {
        val listaActual = _listaDeElementosEnPedido.value.toMutableList()
        val indiceExistente = listaActual.indexOfFirst { elementoActual ->
            elementoActual.producto.identificadorProducto == productoSeleccionado.identificadorProducto
        }

        if (indiceExistente != -1) {
            val elementoExistente = listaActual[indiceExistente]
            listaActual[indiceExistente] = elementoExistente.copy(
                cantidadSolicitada = elementoExistente.cantidadSolicitada + 1
            )
        } else {
            listaActual.add(ElementoPedidoActual(producto = productoSeleccionado, cantidadSolicitada = 1))
        }

        _listaDeElementosEnPedido.value = listaActual
    }
    //agregar algun prodcuto a un pedido en preparacion
    fun incrementarCantidadDeElemento(elementoAIncrementar: ElementoPedidoActual) {
        val listaActual = _listaDeElementosEnPedido.value.toMutableList()
        val indiceEncontrado = listaActual.indexOfFirst { elementoActual ->
            elementoActual.producto.identificadorProducto == elementoAIncrementar.producto.identificadorProducto
        }

        if (indiceEncontrado != -1) {
            val elementoEncontrado = listaActual[indiceEncontrado]
            listaActual[indiceEncontrado] = elementoEncontrado.copy(
                cantidadSolicitada = elementoEncontrado.cantidadSolicitada + 1
            )
            _listaDeElementosEnPedido.value = listaActual
        }
    }
    //quitar algun producto de un pedido
    fun decrementarCantidadDeElemento(elementoADecrementar: ElementoPedidoActual) {
        val listaActual = _listaDeElementosEnPedido.value.toMutableList()
        val indiceEncontrado = listaActual.indexOfFirst { elementoActual ->
            elementoActual.producto.identificadorProducto == elementoADecrementar.producto.identificadorProducto
        }

        if (indiceEncontrado != -1) {
            val elementoEncontrado = listaActual[indiceEncontrado]
            if (elementoEncontrado.cantidadSolicitada > 1) {
                listaActual[indiceEncontrado] = elementoEncontrado.copy(
                    cantidadSolicitada = elementoEncontrado.cantidadSolicitada - 1
                )
            } else {
                listaActual.removeAt(indiceEncontrado)
            }
            _listaDeElementosEnPedido.value = listaActual
        }
    }
    //agregar o quitar ingredientes a un pedido
    fun actualizarModificadoresDeElemento(elementoAEditar: ElementoPedidoActual, nuevosModificadores: String) {
        val listaActual = _listaDeElementosEnPedido.value.toMutableList()
        val indiceEncontrado = listaActual.indexOfFirst { elementoActual ->
            elementoActual.producto.identificadorProducto == elementoAEditar.producto.identificadorProducto
        }

        if (indiceEncontrado != -1) {
            listaActual[indiceEncontrado] = listaActual[indiceEncontrado].copy(
                modificadoresTexto = nuevosModificadores
            )
            _listaDeElementosEnPedido.value = listaActual
        }
    }

    fun actualizarNombreDelCliente(nuevoNombre: String) {
        _nombreDelClienteInput.value = nuevoNombre
    }


    //Cambiar de donde se hizo el pedido (Mostrador o WhatsApp).
    fun actualizarOrigenDelPedido(nuevoOrigen: String) {
        _origenDelPedidoSeleccionado.value = nuevoOrigen
    }

    //cambiar el metodo de pago
    fun actualizarMetodoDePago(nuevoMetodo: String) {
        _metodoDePagoSeleccionado.value = nuevoMetodo
    }

    fun calcularMontoTotalDelPedido(): Double {
        var sumaAcumulada = 0.0
        for (elementoActual in _listaDeElementosEnPedido.value) {
            sumaAcumulada += (elementoActual.producto.precioProducto * elementoActual.cantidadSolicitada)
        }
        return sumaAcumulada
    }

    //guardar comanda en BD
    fun confirmarYGuardarComanda(alFinalizarExitosamente: () -> Unit) {
        if (_listaDeElementosEnPedido.value.isEmpty()) return

        val resumenDetallesConstruido = StringBuilder()
        for (elementoActual in _listaDeElementosEnPedido.value) {
            resumenDetallesConstruido.append("${elementoActual.cantidadSolicitada}x ${elementoActual.producto.nombreProducto}")
            if (elementoActual.modificadoresTexto.isNotBlank()) {
                resumenDetallesConstruido.append(" (${elementoActual.modificadoresTexto})")
            }
            resumenDetallesConstruido.append("; ")
        }

        val nombreFinalCliente = if (_nombreDelClienteInput.value.isNotBlank()) {
            _nombreDelClienteInput.value
        } else {
            "Cliente General"
        }

        val nuevaComanda = Comanda(
            nombreCliente = nombreFinalCliente,
            origenPedido = _origenDelPedidoSeleccionado.value,
            estadoComanda = "Pendiente",
            metodoDePago = _metodoDePagoSeleccionado.value,
            montoTotal = calcularMontoTotalDelPedido(),
            detallesEspecificos = resumenDetallesConstruido.toString()
        )

        viewModelScope.launch {
            repositorioYoliPOS.insertarComanda(nuevaComanda)
            limpiarFormularioDePedido()
            alFinalizarExitosamente()
        }
    }

    //reiniica la pantalla donde se ingresan los datos del pedido
    fun limpiarFormularioDePedido() {
        _listaDeElementosEnPedido.value = emptyList()
        _nombreDelClienteInput.value = ""
        _origenDelPedidoSeleccionado.value = "Mostrador"
        _metodoDePagoSeleccionado.value = "Efectivo"
    }

    //muestra el menu inciial
    fun precargarMenuInicialSiEstaVacio() {
        viewModelScope.launch {
            if (_listaDeProductosDisponibles.value.isEmpty()) {
                val menuInicial = listOf(
                    Producto(nombreProducto = "Torta de Jamón", precioProducto = 45.0, categoriaProducto = "Tortas", rutaImagenProducto = "https://picsum.photos/id/1080/400/400"),
                    Producto(nombreProducto = "Torta Cubana", precioProducto = 70.0, categoriaProducto = "Tortas", rutaImagenProducto = "https://picsum.photos/id/1025/400/400"),
                    Producto(nombreProducto = "Torta de Milanesa", precioProducto = 65.0, categoriaProducto = "Tortas", rutaImagenProducto = "https://picsum.photos/id/1062/400/400"),
                    Producto(nombreProducto = "Refresco de Cola 600ml", precioProducto = 25.0, categoriaProducto = "Bebidas", rutaImagenProducto = "https://picsum.photos/id/1060/400/400"),
                    Producto(nombreProducto = "Agua Horchata Natural", precioProducto = 30.0, categoriaProducto = "Bebidas", rutaImagenProducto = "https://picsum.photos/id/1084/400/400"),
                    Producto(nombreProducto = "Papas a la Francesa", precioProducto = 35.0, categoriaProducto = "Acompañamientos", rutaImagenProducto = "https://picsum.photos/id/1082/400/400")
                )
                repositorioYoliPOS.insertarListaDeProductos(menuInicial)
            }
        }
    }
}