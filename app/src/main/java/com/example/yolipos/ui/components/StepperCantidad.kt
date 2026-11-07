package com.example.yolipos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yolipos.Greeting
import com.example.yolipos.ui.theme.YoliPOSTheme

// Componente de interfaz que permite incrementar o decrementar la cantidad de un producto
 // mediante controles directos (+ / -) con visualizacion numerica.
@Composable
fun StepperCantidad(
    cantidadActual: Int,
    alIncrementarCantidad: () -> Unit,
    alDecrementarCantidad: () -> Unit,
    modificadorVisual: Modifier = Modifier
) {
    Row(
        modifier = modificadorVisual
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón de decremento de cantidad (-)
        IconButton(
            onClick = { alDecrementarCantidad() }
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrementar cantidad de productos",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Indicador numérico visual de la cantidad seleccionada
        Text(
            text = cantidadActual.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // Botón de incremento de cantidad (+)
        IconButton(
            onClick = { alIncrementarCantidad() }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Incrementar cantidad de productos",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrevisualizacionStepperCantidad() {
    StepperCantidad(
        cantidadActual = 3,
        alIncrementarCantidad = {},
        alDecrementarCantidad = {}
    )
}