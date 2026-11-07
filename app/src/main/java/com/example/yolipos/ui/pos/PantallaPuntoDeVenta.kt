package com.example.yolipos.ui.pos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.yolipos.model.Producto
import com.example.yolipos.ui.components.GrupoChipsSeleccionUnica
import com.example.yolipos.ui.components.StepperCantidad
import com.example.yolipos.viewmodel.ElementoPedidoActual
import com.example.yolipos.viewmodel.PuntoDeVentaViewModel

 // Pantalla principal del punto de venta en mostrador para seleccionar platillos,
 // personalizar ingredientes, ajustar cantidades y confirmar la comanda.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPuntoDeVenta(
    puntoDeVentaViewModel: PuntoDeVentaViewModel,
    alConfirmarComandaExitosa: () -> Unit,
    modificadorVisual: Modifier = Modifier
) {
    val listaDeProductosDisponibles by puntoDeVentaViewModel.listaDeProductosDisponibles.collectAsState()
    val categoriaSeleccionada by puntoDeVentaViewModel.categoriaSeleccionada
    val listaDeElementosEnPedido by puntoDeVentaViewModel.listaDeElementosEnPedido.collectAsState()
    val nombreDelClienteInput by puntoDeVentaViewModel.nombreDelClienteInput
    val origenDelPedidoSeleccionado by puntoDeVentaViewModel.origenDelPedidoSeleccionado
    val metodoDePagoSeleccionado by puntoDeVentaViewModel.metodoDePagoSeleccionado

    val listaDeCategorias = listOf("Todas", "Tortas", "Bebidas", "Acompañamientos")
    val listaDeOrigenes = listOf("Mostrador", "WhatsApp")
    val listaDeMetodosDePago = listOf("Efectivo", "Transferencia")

    LaunchedEffect(Unit) {
        puntoDeVentaViewModel.precargarMenuInicialSiEstaVacio()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nueva Comanda - Mostrador",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { valoresDeRelleno ->
        Column(
            modifier = modificadorVisual
                .fillMaxSize()
                .padding(valoresDeRelleno)
                .padding(16.dp)
        ) {
            // Filtro por categoría de producto
            GrupoChipsSeleccionUnica(
                listaDeOpcionesDisponibles = listaDeCategorias,
                opcionSeleccionadaActual = categoriaSeleccionada,
                alSeleccionarNuevaOpcion = { nuevaCategoriaSeleccionada ->
                    puntoDeVentaViewModel.cambiarCategoriaSeleccionada(nuevaCategoriaSeleccionada)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sección del catálogo (arriba) y resumen del pedido (abajo)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Cuadrícula de productos del catálogo
                Text(
                    text = "Menú Disponible:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = listaDeProductosDisponibles,
                        key = { productoActual -> productoActual.identificadorProducto }
                    ) { productoActual ->
                        TarjetaProductoCatalogo(
                            producto = productoActual,
                            alSeleccionarProducto = {
                                puntoDeVentaViewModel.agregarProductoAPedido(productoActual)
                            }
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Resumen del carrito y datos del cliente
                Text(
                    text = "Resumen de la Orden (${listaDeElementosEnPedido.size} ítems):",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (listaDeElementosEnPedido.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(0.8f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Selecciona platillos del catálogo para agregar al pedido.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(0.8f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(
                            items = listaDeElementosEnPedido,
                            key = { elementoActual -> elementoActual.producto.identificadorProducto }
                        ) { elementoActual ->
                            FilaElementoPedido(
                                elementoPedido = elementoActual,
                                alIncrementar = {
                                    puntoDeVentaViewModel.incrementarCantidadDeElemento(elementoActual)
                                },
                                alDecrementar = {
                                    puntoDeVentaViewModel.decrementarCantidadDeElemento(elementoActual)
                                }
                            )
                        }
                    }
                }

                // Campo para el nombre del cliente
                OutlinedTextField(
                    value = nombreDelClienteInput,
                    onValueChange = { textoIngresado ->
                        puntoDeVentaViewModel.actualizarNombreDelCliente(textoIngresado)
                    },
                    label = { Text("Nombre del cliente") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Origen:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        GrupoChipsSeleccionUnica(
                            listaDeOpcionesDisponibles = listaDeOrigenes,
                            opcionSeleccionadaActual = origenDelPedidoSeleccionado,
                            alSeleccionarNuevaOpcion = { nuevoOrigen ->
                                puntoDeVentaViewModel.actualizarOrigenDelPedido(nuevoOrigen)
                            }
                        )
                    }

                    Column {
                        Text(text = "Pago:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        GrupoChipsSeleccionUnica(
                            listaDeOpcionesDisponibles = listaDeMetodosDePago,
                            opcionSeleccionadaActual = metodoDePagoSeleccionado,
                            alSeleccionarNuevaOpcion = { nuevoMetodo ->
                                puntoDeVentaViewModel.actualizarMetodoDePago(nuevoMetodo)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Total acumulado y botón de confirmación
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total: $${puntoDeVentaViewModel.calcularMontoTotalDelPedido()}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Button(
                        onClick = {
                            puntoDeVentaViewModel.confirmarYGuardarComanda(
                                alFinalizarExitosamente = alConfirmarComandaExitosa
                            )
                        },
                        enabled = listaDeElementosEnPedido.isNotEmpty()
                    ) {
                        Text("Confirmar Orden")
                    }
                }
            }
        }
    }
}

 // Tarjeta individual para desplegar un producto dentro del catálogo del punto de venta.
@Composable
fun TarjetaProductoCatalogo(
    producto: Producto,
    alSeleccionarProducto: () -> Unit,
    modificadorVisual: Modifier = Modifier
) {
    Card(
        modifier = modificadorVisual
            .fillMaxWidth()
            .clickable { alSeleccionarProducto() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = producto.rutaImagenProducto,
                contentDescription = producto.nombreProducto,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = producto.nombreProducto,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = "$${producto.precioProducto}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

 // Fila individual dentro del carrito de compra que muestra el producto, precio y stepper de cantidad.
@Composable
fun FilaElementoPedido(
    elementoPedido: ElementoPedidoActual,
    alIncrementar: () -> Unit,
    alDecrementar: () -> Unit,
    modificadorVisual: Modifier = Modifier
) {
    Row(
        modifier = modificadorVisual
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = elementoPedido.producto.nombreProducto,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "$${elementoPedido.producto.precioProducto} c/u",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        StepperCantidad(
            cantidadActual = elementoPedido.cantidadSolicitada,
            alIncrementarCantidad = alIncrementar,
            alDecrementarCantidad = alDecrementar
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrevisualizacionTarjetaProductoCatalogo() {
    val productoEjemplo = Producto(
        identificadorProducto = 1,
        nombreProducto = "Torta Cubana",
        precioProducto = 70.0,
        categoriaProducto = "Tortas",
        rutaImagenProducto = "https://picsum.photos/id/1025/400/400"
    )
    TarjetaProductoCatalogo(
        producto = productoEjemplo,
        alSeleccionarProducto = {}
    )
}