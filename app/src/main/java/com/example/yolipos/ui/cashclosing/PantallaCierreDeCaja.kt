package com.example.yolipos.ui.cashclosing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yolipos.model.Comanda
import com.example.yolipos.viewmodel.CierreDeCajaViewModel

 // Pantalla de resumen de ventas y cierre de caja que muestra el desglose
 // de ingresos por efectivo, transferencia y la administración del turno.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCierreDeCaja(
    cierreDeCajaViewModel: CierreDeCajaViewModel,
    modificadorVisual: Modifier = Modifier
) {
    val listaDeComandasEnRango by cierreDeCajaViewModel.listaDeComandasEnRango.collectAsState()
    val totalEfectivoCalculado by cierreDeCajaViewModel.totalEfectivoCalculado
    val totalTransferenciaCalculado by cierreDeCajaViewModel.totalTransferenciaCalculado
    val totalVentasGlobalCalculado by cierreDeCajaViewModel.totalVentasGlobalCalculado

    val nombreCajeroActual by cierreDeCajaViewModel.nombreCajeroActual
    val cajaAbiertaActualmente by cierreDeCajaViewModel.cajaAbiertaActualmente
    val horaAperturaTurno by cierreDeCajaViewModel.horaAperturaTurno

    var campoNombreCajeroInput by remember { mutableStateOf(nombreCajeroActual) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Arqueo y Cierre de Caja",
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
            // Tarjeta de estado de turno y cajero
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Estado del Turno Actual",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (cajaAbiertaActualmente) {
                        Text(
                            text = "Cajero en Servicio: $nombreCajeroActual",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Hora de Apertura: $horaAperturaTurno",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { cierreDeCajaViewModel.cerrarTurnoDeCaja() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cerrar Turno de Caja")
                        }
                    } else {
                        Text(
                            text = "La caja se encuentra cerrada.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = campoNombreCajeroInput,
                            onValueChange = { textoNuevo -> campoNombreCajeroInput = textoNuevo },
                            label = { Text("Nombre del Cajero") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                if (campoNombreCajeroInput.isNotBlank()) {
                                    val horaActualFormateada = cierreDeCajaViewModel.formatearMilisegundosAFechaTexto(System.currentTimeMillis())
                                    cierreDeCajaViewModel.abrirTurnoDeCaja(
                                        nombreDelCajero = campoNombreCajeroInput,
                                        horaActualTexto = horaActualFormateada
                                    )
                                }
                            },
                            enabled = campoNombreCajeroInput.isNotBlank(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Abrir Nuevo Turno")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjetas del resumen financiero
            Text(
                text = "Resumen de Conciliación",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TarjetaMetricaFinanciera(
                    tituloMetrica = "Efectivo",
                    montoMetrica = totalEfectivoCalculado,
                    colorFondo = Color(0xFFE8F5E9),
                    colorTexto = Color(0xFF2E7D32),
                    modificadorVisual = Modifier.weight(1f)
                )

                TarjetaMetricaFinanciera(
                    tituloMetrica = "Transferencia",
                    montoMetrica = totalTransferenciaCalculado,
                    colorFondo = Color(0xFFE3F2FD),
                    colorTexto = Color(0xFF1565C0),
                    modificadorVisual = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TarjetaMetricaFinanciera(
                tituloMetrica = "Total Ventas Registradas",
                montoMetrica = totalVentasGlobalCalculado,
                colorFondo = MaterialTheme.colorScheme.primaryContainer,
                colorTexto = MaterialTheme.colorScheme.onPrimaryContainer,
                modificadorVisual = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de transacciones en el periodo
            Text(
                text = "Historial de Comandas del Periodo (${listaDeComandasEnRango.size}):",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (listaDeComandasEnRango.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron comandes registradas en este turno.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = listaDeComandasEnRango,
                        key = { comandaActual -> comandaActual.identificadorComanda }
                    ) { comandaActual ->
                        FilaResumenComandaAuditoria(comanda = comandaActual)
                    }
                }
            }
        }
    }
}

 // Componente interno que representa una tarjeta de métrica financiera rápida (Efectivo/Transferencia/Total).
@Composable
fun TarjetaMetricaFinanciera(
    tituloMetrica: String,
    montoMetrica: Double,
    colorFondo: Color,
    colorTexto: Color,
    modificadorVisual: Modifier = Modifier
) {
    Card(
        modifier = modificadorVisual,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = tituloMetrica,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorTexto
            )
            Text(
                text = "$${montoMetrica}",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colorTexto
            )
        }
    }
}

 //Componente que muestra una fila compacta de la comanda dentro de la lista de auditoría.
@Composable
fun FilaResumenComandaAuditoria(
    comanda: Comanda,
    modificadorVisual: Modifier = Modifier
) {
    Card(
        modifier = modificadorVisual.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${comanda.nombreCliente} (${comanda.origenPedido})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Pago: ${comanda.metodoDePago} | Estado: ${comanda.estadoComanda}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "$${comanda.montoTotal}",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrevisualizacionPantallaCierreDeCaja() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TarjetaMetricaFinanciera(
                tituloMetrica = "Efectivo",
                montoMetrica = 450.0,
                colorFondo = Color(0xFFE8F5E9),
                colorTexto = Color(0xFF2E7D32),
                modificadorVisual = Modifier.weight(1f)
            )
            TarjetaMetricaFinanciera(
                tituloMetrica = "Transferencia",
                montoMetrica = 280.0,
                colorFondo = Color(0xFFE3F2FD),
                colorTexto = Color(0xFF1565C0),
                modificadorVisual = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        FilaResumenComandaAuditoria(
            comanda = Comanda(
                identificadorComanda = 1,
                nombreCliente = "Gonzalo López",
                origenPedido = "WhatsApp",
                estadoComanda = "Listo",
                metodoDePago = "Efectivo",
                montoTotal = 175.0,
                detallesEspecificos = "2x Torta Cubana;"
            )
        )
    }
}