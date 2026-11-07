package com.example.yolipos.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yolipos.model.Comanda

//componente visual que muestra una comanda en el tablero de cocina

@Composable
fun TarjetaComanda(
    comanda: Comanda,
    alCambiarEstadoComanda: (String) -> Unit,
    alEliminarComanda: (Comanda) -> Unit,
    modificadorVisual: Modifier = Modifier
) {
    val colorContenedorEstado = when (comanda.estadoComanda) {
        "Pendiente" -> Color(0xFFFFEBEE) // Rojo suave
        "En preparación" -> Color(0xFFFFF8E1) // Amarillo suave
        "Listo" -> Color(0xFFE8F5E9) // Verde suave
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val colorTextoEstado = when (comanda.estadoComanda) {
        "Pendiente" -> Color(0xFFC62828)
        "En preparación" -> Color(0xFFF57F17)
        "Listo" -> Color(0xFF2E7D32)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val textoSiguienteEstado = when (comanda.estadoComanda) {
        "Pendiente" -> "Iniciar preparación"
        "En preparación" -> "Marcar como listo"
        "Listo" -> "Entregar orden"
        else -> "Finalizado"
    }

    Card(
        modifier = modificadorVisual.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorContenedorEstado),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            //encabezado cliente y origen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = comanda.nombreCliente,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Origen: ${comanda.origenPedido} | Pago: ${comanda.metodoDePago}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = { alEliminarComanda(comanda) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar comanda",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Estado visual destacado
        Text(
            text = "Estado: ${comanda.estadoComanda}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = colorTextoEstado
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Detalles de la orden e ingredientes modificados
        Text(
            text = "Platillos y modificaciones:",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = comanda.detallesEspecificos.ifBlank { "Sin detalles adicionales" },
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Pie de tarjeta: Total gastado y Botón de avance de estado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total: $${comanda.montoTotal}",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            if (comanda.estadoComanda != "Entregado") {
                Button(
                    onClick = {
                        val nuevoEstado = when (comanda.estadoComanda) {
                            "Pendiente" -> "En preparación"
                            "En preparación" -> "Listo"
                            "Listo" -> "Entregado"
                            else -> "Entregado"
                        }
                        alCambiarEstadoComanda(nuevoEstado)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorTextoEstado)
                ) {
                    Text(text = textoSiguienteEstado, color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrevisualizacionTarjetaComanda() {
    val comandaDePrueba = Comanda(
        identificadorComanda = 1,
        nombreCliente = "Alberto Torres",
        origenPedido = "WhatsApp",
        estadoComanda = "En preparación",
        metodoDePago = "Efectivo",
        montoTotal = 135.0,
        detallesEspecificos = "1x Torta Cubana (Sin cebolla); 1x Refresco de Cola 600ml;"
    )
    TarjetaComanda(
        comanda = comandaDePrueba,
        alCambiarEstadoComanda = {},
        alEliminarComanda = {}
    )
}