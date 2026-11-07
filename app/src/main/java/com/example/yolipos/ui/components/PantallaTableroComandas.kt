package com.example.yolipos.ui.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yolipos.model.Comanda
import com.example.yolipos.ui.components.GrupoChipsSeleccionUnica
import com.example.yolipos.ui.components.TarjetaComanda
import com.example.yolipos.viewmodel.ComandasViewModel

 // Pantalla principal de control de comandas para la cocina, desplegando
 // el listado dinámico de pedidos ordenados por estado y prioridad de atención.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTableroComandas(
    comandasViewModel: ComandasViewModel,
    alNavegarANuevaComanda: () -> Unit,
    modificadorVisual: Modifier = Modifier
) {
    val listaDeComandasActual by comandasViewModel.listaDeComandas.collectAsState()
    val filtroEstadoActual by comandasViewModel.filtroEstadoSeleccionado
    val estaCargandoInformacion by comandasViewModel.estaCargandoInformacion

    val opcionesDeFiltro = listOf("Todos", "Pendiente", "En preparación", "Listo")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tablero de Cocina",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(
                        onClick = { comandasViewModel.cargarComandasSegunFiltro() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar lista de comandas"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { alNavegarANuevaComanda() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar nueva comanda desde mostrador"
                )
            }
        }
    ) { valoresDeRelleno ->
        Column(
            modifier = modificadorVisual
                .fillMaxSize()
                .padding(valoresDeRelleno)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Barra de filtrado por estado
            Text(
                text = "Filtrar por estado:",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            GrupoChipsSeleccionUnica(
                listaDeOpcionesDisponibles = opcionesDeFiltro,
                opcionSeleccionadaActual = filtroEstadoActual,
                alSeleccionarNuevaOpcion = { nuevoEstadoSeleccionado ->
                    comandasViewModel.cambiarFiltroDeEstado(nuevoEstadoSeleccionado)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Indicador de carga o lista principal
            if (estaCargandoInformacion) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (listaDeComandasActual.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay comandas registradas en este estado.",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(
                        items = listaDeComandasActual,
                        key = { comandaActual -> comandaActual.identificadorComanda }
                    ) { comandaActual ->
                        TarjetaComanda(
                            comanda = comandaActual,
                            alCambiarEstadoComanda = { nuevoEstadoOperativo ->
                                comandasViewModel.cambiarEstadoDeComanda(
                                    identificadorComanda = comandaActual.identificadorComanda,
                                    nuevoEstadoOperativo = nuevoEstadoOperativo
                                )
                            },
                            alEliminarComanda = { comandaAEliminar ->
                                comandasViewModel.eliminarComandaExistente(comandaAEliminar)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrevisualizacionContenidoTableroComandas() {
    val listaEjemploComandas = listOf(
        Comanda(
            identificadorComanda = 1,
            nombreCliente = "Alberto Torres",
            origenPedido = "WhatsApp",
            estadoComanda = "Pendiente",
            metodoDePago = "Efectivo",
            montoTotal = 95.0,
            detallesEspecificos = "1x Torta Cubana (Sin mayonesa); 1x Refresco;"
        ),
        Comanda(
            identificadorComanda = 2,
            nombreCliente = "Erik Ortiz",
            origenPedido = "Mostrador",
            estadoComanda = "En preparación",
            metodoDePago = "Transferencia",
            montoTotal = 110.0,
            detallesEspecificos = "2x Torta de Milanesa (Sin cebolla);"
        )
    )

    Column(modifier = Modifier.padding(16.dp)) {
        GrupoChipsSeleccionUnica(
            listaDeOpcionesDisponibles = listOf("Todos", "Pendiente", "En preparación", "Listo"),
            opcionSeleccionadaActual = "Todos",
            alSeleccionarNuevaOpcion = {}
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(listaEjemploComandas) { comandaActual ->
                TarjetaComanda(
                    comanda = comandaActual,
                    alCambiarEstadoComanda = {},
                    alEliminarComanda = {}
                )
            }
        }
    }
}