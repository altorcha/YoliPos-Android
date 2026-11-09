package com.example.yolipos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yolipos.data.local.BaseDeDatosApp
import com.example.yolipos.data.local.GestorPreferencias
import com.example.yolipos.domain.RepositorioYoliPOS
import com.example.yolipos.ui.navigation.NavegacionYoliPOS
import com.example.yolipos.ui.theme.YoliPOSTheme
import com.example.yolipos.viewmodel.CierreDeCajaViewModel
import com.example.yolipos.viewmodel.ComandasViewModel
import com.example.yolipos.viewmodel.PuntoDeVentaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Inicialización del acceso local a datos y preferencias del turno
        val baseDeDatosApp = BaseDeDatosApp.obtenerBaseDeDatos(applicationContext)
        val gestorPreferencias = GestorPreferencias(applicationContext)
        val repositorioYoliPOS = RepositorioYoliPOS(
            objetoComandaDao = baseDeDatosApp.obtenerComandaDao(),
            objetoProductoDao = baseDeDatosApp.obtenerProductoDao()
        )

        // Factoría de ViewModels para la inyección de dependencias sin frameworks externos
        val factoriaViewModels = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <ClaseSolicitada : ViewModel> create(modelClass: Class<ClaseSolicitada>): ClaseSolicitada {
                return when {
                    modelClass.isAssignableFrom(ComandasViewModel::class.java) -> {
                        ComandasViewModel(repositorioYoliPOS) as ClaseSolicitada
                    }

                    modelClass.isAssignableFrom(PuntoDeVentaViewModel::class.java) -> {
                        PuntoDeVentaViewModel(repositorioYoliPOS) as ClaseSolicitada
                    }

                    modelClass.isAssignableFrom(CierreDeCajaViewModel::class.java) -> {
                        CierreDeCajaViewModel(
                            repositorioYoliPOS,
                            gestorPreferencias
                        ) as ClaseSolicitada
                    }

                    else -> throw IllegalArgumentException("Clase ViewModel no soportada en la factoría")
                }
            }
        }

        // Instanciación de los ViewModels ligados al ciclo de vida de la actividad
        val comandasViewModel =
            ViewModelProvider(this, factoriaViewModels)[ComandasViewModel::class.java]
        val puntoDeVentaViewModel =
            ViewModelProvider(this, factoriaViewModels)[PuntoDeVentaViewModel::class.java]
        val cierreDeCajaViewModel =
            ViewModelProvider(this, factoriaViewModels)[CierreDeCajaViewModel::class.java]
        setContent {
            YoliPOSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavegacionYoliPOS(
                        comandasViewModel = comandasViewModel,
                        puntoDeVentaViewModel = puntoDeVentaViewModel,
                        cierreDeCajaViewModel = cierreDeCajaViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    YoliPOSTheme {
        Greeting("Android")
    }
}