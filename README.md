# YoliPOS

**Control ágil para la cocina tradicional.**

Aplicación móvil nativa para Android desarrollada para el negocio "Tortas Yoli", que centraliza la recepción de comandas (WhatsApp y mostrador), el punto de venta y el corte de caja diario. En etapas posteriores se integrará con un backend propio en Node.js y con Firebase Cloud Messaging para notificaciones push en tiempo real.

**Equipo:** La 4T
**Integrantes:** Alberto Torres Chaparro (Líder), Erik Ortiz Leal, Luis Mario Baldenebro Quiroz, Angel Gustavo Pacheco Meza

## Ficha técnica

| Elemento | Detalle |
|---|---|
| Lenguaje de programación | Kotlin (gestionado vía catálogo de versiones, ver `gradle/libs.versions.toml`) |
| IDE | Android Studio Quail \| 2026.1.3 (Build #AI-261.26222.65.2613.15948027) |
| SDK mínimo (minSdk) | API 24 (Android 7.0) |
| SDK objetivo (targetSdk) | API 37 |
| SDK de compilación (compileSdk) | API 37 |
| Compatibilidad de Java | JavaVersion 11 |
| Gestor de dependencias | Gradle (Kotlin DSL - build.gradle.kts + catálogo de versiones libs.versions.toml) |
| Framework de UI | Jetpack Compose |
| Namespace / Application ID | com.example.yolipos |
| Arquitectura planeada | MVVM (Model-View-ViewModel) |

## Dependencias actuales

- androidx.compose (BoM) — plataforma de versiones alineadas de Compose
- androidx.activity:activity-compose — integración de Activity con Compose
- androidx.compose.material3 — componentes de UI Material 3
- androidx.compose.ui / ui-graphics / ui-tooling-preview — núcleo de Jetpack Compose
- androidx.core:core-ktx — extensiones de Kotlin para AndroidX
- androidx.lifecycle:lifecycle-runtime-ktx — ciclo de vida con corrutinas
- junit, androidx.test.espresso:espresso-core, androidx.test.ext:junit — pruebas unitarias e instrumentadas
- androidx.compose.ui:ui-test-junit4 / ui-test-manifest / ui-tooling (debug) — herramientas de testing y depuración de Compose

**Próximas dependencias planeadas** (no agregadas aún, se incorporarán conforme avance el desarrollo):
- Firebase Cloud Messaging — notificaciones push de nuevas comandas
- Retrofit + converter-gson — consumo del backend en Node.js
- Room — persistencia local de comandas y corte de caja

## Estructura del proyecto

```
app/src/main/java/com/example/yolipos/
├── ui/
│   ├── orders/       # Composables del tablero de comandas (Pendiente / En preparación / Listo)
│   ├── pos/          # Composables del punto de venta rápido
│   ├── cashclosing/  # Composables del corte de caja diario
│   └── theme/        # Color.kt, Theme.kt, Type.kt (tema de Compose)
├── data/
│   ├── remote/       # Consumo del backend Node.js (Retrofit, próximamente)
│   ├── local/        # Persistencia local (Room, próximamente)
│   └── notifications/# Integración con Firebase Cloud Messaging (próximamente)
├── domain/           # Casos de uso (calcular corte de caja, validar pedido, etc.)
├── model/            # Entidades: Order, Product, CashClosing, etc.
├── viewmodel/        # ViewModels de cada módulo
└── utils/            # Funciones auxiliares y constantes
```

## Cómo ejecutar el proyecto

1. Clonar el repositorio: `git clone https://github.com/altorcha/YoliPos-Android.git`
2. Abrir el proyecto en Android Studio Quail | 2026.1.3 o superior.
3. Sincronizar Gradle (Sync Now).
4. Ejecutar en un emulador o dispositivo físico con Android 7.0 (API 24) o superior.

## Contexto del problema que resuelve

"Tortas Yoli" carece de un sistema centralizado para registrar comandas y transacciones financieras en tiempo real, lo que genera desorganización en cocina, demoras en el servicio y pérdidas monetarias por ausencia de controles de caja formales. YoliPOS busca resolver esto mediante comandas en tiempo real, un punto de venta rápido y un módulo de corte de caja automatizado.
