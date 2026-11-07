package com.example.yolipos.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

 // Componente de interfaz que despliega una lista de opciones en forma de FilterChips
 // de selección múltiple, ideal para la personalización de ingredientes en el pedido.
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GrupoChipsSeleccionMultiple(
    listaDeOpcionesDisponibles: List<String>,
    listaDeOpcionesSeleccionadas: List<String>,
    alCambiarSeleccionDeOpcion: (String) -> Unit,
    modificadorVisual: Modifier = Modifier
) {
    FlowRow(
        modifier = modificadorVisual.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (opcionActual in listaDeOpcionesDisponibles) {
            val estaSeleccionadaLaOpcion = listaDeOpcionesSeleccionadas.contains(opcionActual)
            FilterChip(
                selected = estaSeleccionadaLaOpcion,
                onClick = { alCambiarSeleccionDeOpcion(opcionActual) },
                label = {
                    Text(
                        text = opcionActual,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

 // Componente de interfaz que despliega una lista de opciones en forma de FilterChips
 // de selección única y mutuamente excluyente (ej. Origen del pedido o Filtro de estado).
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GrupoChipsSeleccionUnica(
    listaDeOpcionesDisponibles: List<String>,
    opcionSeleccionadaActual: String,
    alSeleccionarNuevaOpcion: (String) -> Unit,
    modificadorVisual: Modifier = Modifier
) {
    FlowRow(
        modifier = modificadorVisual.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (opcionActual in listaDeOpcionesDisponibles) {
            val estaSeleccionadaLaOpcion = (opcionActual == opcionSeleccionadaActual)
            FilterChip(
                selected = estaSeleccionadaLaOpcion,
                onClick = { alSeleccionarNuevaOpcion(opcionActual) },
                label = {
                    Text(
                        text = opcionActual,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrevisualizacionModificadorChips() {
    Column(modifier = Modifier.padding(16.dp)) {
        GrupoChipsSeleccionMultiple(
            listaDeOpcionesDisponibles = listOf("Sin cebolla", "Sin mayonesa", "Extra queso"),
            listaDeOpcionesSeleccionadas = listOf("Sin cebolla"),
            alCambiarSeleccionDeOpcion = {}
        )
        GrupoChipsSeleccionUnica(
            listaDeOpcionesDisponibles = listOf("Mostrador", "WhatsApp"),
            opcionSeleccionadaActual = "Mostrador",
            alSeleccionarNuevaOpcion = {}
        )
    }
}