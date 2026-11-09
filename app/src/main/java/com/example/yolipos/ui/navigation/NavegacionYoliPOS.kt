package com.example.yolipos.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.yolipos.ui.cashclosing.PantallaCierreDeCaja
import com.example.yolipos.ui.orders.PantallaTableroComandas
import com.example.yolipos.ui.pos.PantallaPuntoDeVenta
import com.example.yolipos.viewmodel.CierreDeCajaViewModel
import com.example.yolipos.viewmodel.ComandasViewModel
import com.example.yolipos.viewmodel.PuntoDeVentaViewModel

 // Define los destinos de navegación disponibles dentro de la aplicación YoliPOS.

sealed class DestinoNavegacion(
    val rutaNavegacion: String,
    val tituloEtiqueta: String,
    val iconoVectorial: ImageVector
) {
    object TableroCocina : DestinoNavegacion(
        rutaNavegacion = "pantalla_tablero_cocina",
        tituloEtiqueta = "Cocina",
        iconoVectorial = Icons.Default.RestaurantMenu
    )

    object PuntoDeVenta : DestinoNavegacion(
        rutaNavegacion = "pantalla_punto_de_venta",
        tituloEtiqueta = "Mostrador",
        iconoVectorial = Icons.Default.PointOfSale
    )

    object CierreDeCaja : DestinoNavegacion(
        rutaNavegacion = "pantalla_cierre_de_caja",
        tituloEtiqueta = "Caja",
        iconoVectorial = Icons.Default.AccountBalanceWallet
    )
}

 // Contenedor principal de navegación que integra la barra inferior NavigationBar
 // y el NavHost para la conmutación entre pantallas de la aplicación.
@Composable
fun NavegacionYoliPOS(
    comandasViewModel: ComandasViewModel,
    puntoDeVentaViewModel: PuntoDeVentaViewModel,
    cierreDeCajaViewModel: CierreDeCajaViewModel,
    controladorNavegacion: NavHostController = rememberNavController(),
    modificadorVisual: Modifier = Modifier
) {
    val listaDeDestinos = listOf(
        DestinoNavegacion.TableroCocina,
        DestinoNavegacion.PuntoDeVenta,
        DestinoNavegacion.CierreDeCaja
    )

    val entradaNavegacionActual by controladorNavegacion.currentBackStackEntryAsState()
    val rutaSeleccionadaActual = entradaNavegacionActual?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                for (destinoActual in listaDeDestinos) {
                    val estaSeleccionadoElDestino = (rutaSeleccionadaActual == destinoActual.rutaNavegacion)
                    NavigationBarItem(
                        selected = estaSeleccionadoElDestino,
                        onClick = {
                            controladorNavegacion.navigate(destinoActual.rutaNavegacion) {
                                popUpTo(controladorNavegacion.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = destinoActual.iconoVectorial,
                                contentDescription = destinoActual.tituloEtiqueta
                            )
                        },
                        label = {
                            Text(text = destinoActual.tituloEtiqueta)
                        }
                    )
                }
            }
        }
    ) { valoresDeRelleno ->
        NavHost(
            navController = controladorNavegacion,
            startDestination = DestinoNavegacion.TableroCocina.rutaNavegacion,
            modifier = modificadorVisual.padding(valoresDeRelleno)
        ) {
            composable(route = DestinoNavegacion.TableroCocina.rutaNavegacion) {
                PantallaTableroComandas(
                    comandasViewModel = comandasViewModel,
                    alNavegarANuevaComanda = {
                        controladorNavegacion.navigate(DestinoNavegacion.PuntoDeVenta.rutaNavegacion)
                    }
                )
            }

            composable(route = DestinoNavegacion.PuntoDeVenta.rutaNavegacion) {
                PantallaPuntoDeVenta(
                    puntoDeVentaViewModel = puntoDeVentaViewModel,
                    alConfirmarComandaExitosa = {
                        controladorNavegacion.navigate(DestinoNavegacion.TableroCocina.rutaNavegacion) {
                            popUpTo(DestinoNavegacion.TableroCocina.rutaNavegacion) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(route = DestinoNavegacion.CierreDeCaja.rutaNavegacion) {
                PantallaCierreDeCaja(
                    cierreDeCajaViewModel = cierreDeCajaViewModel
                )
            }
        }
    }
}