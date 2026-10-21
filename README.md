# AppBase: cascarón de navegación para tu proyecto

App de punto de partida en Kotlin + Jetpack Compose con:

- **Login simulado:** cualquier dato entra a la app.
- **Barra inferior (bottom navigation)** con 5 pantallas: Inicio, Buscar, Avisos, Favoritos y Perfil.
- **Menú lateral (navigation drawer)** con 5 pantallas: Calendario, Mensajes, Reportes, Configuración y Acerca de, más **Cerrar sesión**.
- **Barra superior** con el título de la pantalla actual y el botón ☰ para abrir el menú.

## 1. Abrir el proyecto

1. Descomprime el zip. Queda una carpeta `AppBase`.
2. En Android Studio: **File > Open** y selecciona la carpeta `AppBase` (la que contiene `settings.gradle.kts`, no la carpeta `app`).
3. Espera a que termine el **Gradle Sync** (la primera vez descarga dependencias y tarda unos minutos).
4. Si Android Studio sugiere actualizar el Android Gradle Plugin o alguna dependencia, acepta.
5. Corre la app con ▶ en un emulador o teléfono.

## 2. Para usarlo en tu proyecto

- **Cambiar el nombre de la app:** `app/src/main/res/values/strings.xml` → `app_name`.
- **Cambiar colores:** `ui/theme/Color.kt`. Si quieres que siempre se usen tus colores, pon `dynamicColor = false` en `ui/theme/Theme.kt`.
- **Cambiar el ícono:** clic derecho en `res` > **New > Image Asset**.
- **Cambiar el paquete `mx.tec.appbase`:** clic derecho en el paquete > **Refactor > Rename** y cambia también `namespace` y `applicationId` en `app/build.gradle.kts`.

## 3. Versiones del proyecto

Todas están en `gradle/libs.versions.toml`: Kotlin 2.4.10, Navigation 2.9.8, Compose BOM, compileSdk/targetSdk 36 y minSdk 26. El permiso de Internet ya viene en el `AndroidManifest.xml`.

## 4. Estructura

```
mx.tec.appbase
├── MainActivity.kt              Arranca la app
├── navigation
│   ├── Rutas.kt                 Rutas y pantallas (con título e ícono) + listas de menús
│   ├── AppNavigation.kt         Nivel 1: Login ↔ Principal
│   └── PrincipalNavHost.kt      Nivel 2: las 10 pantallas internas
└── ui
    ├── components
    │   ├── BarraSuperior.kt     TopAppBar con botón ☰
    │   ├── BarraInferior.kt     Bottom navigation
    │   ├── MenuLateral.kt       Navigation drawer
    │   └── PantallaEjemplo.kt   Contenido de relleno de cada pantalla
    ├── screens
    │   ├── LoginScreen.kt
    │   ├── PrincipalScreen.kt   El cascarón: drawer + barras + NavHost
    │   ├── inferior/            Inicio, Buscar, Notificaciones, Favoritos, Perfil
    │   └── lateral/             Calendario, Mensajes, Reportes, Configuracion, AcercaDe
    └── theme                    Colores, tema y tipografía
```

## 5. Cómo agregar y cambiar pantallas a tu proyecto

**Cambiar una pantalla:** abre su archivo (por ejemplo `inferior/InicioScreen.kt`), borra la llamada a `PantallaEjemplo` y escribe tu contenido.

**Renombrar o cambiar el ícono de una opción:** edita su `data object` en `Rutas.kt`. El menú y la barra se actualizan solos.

**Agregar una pantalla nueva:**
1. En `Rutas.kt`, agrega un `data object` dentro de `Pantalla`.
2. Agrégalo a `pantallasBarraInferior` o a `pantallasMenuLateral`.
3. Crea su archivo `...Screen.kt`.
4. Regístrala en `PrincipalNavHost.kt` con `composable(Pantalla.X.route) { XScreen() }`.

**Quitar una opción de un menú:** sácala de la lista en `Rutas.kt`. Si ya no la vas a usar, borra también su `composable` y su archivo.

> La barra inferior admite de **3 a 5** opciones según Material Design. Si necesitas más, mándalas al menú lateral.

## Si algo falla

| Error | Solución |
|---|---|
| No aparece el botón ▶ o no reconoce archivos | Abriste la carpeta equivocada. Usa **File > Open** sobre `AppBase`, no sobre `app`. |
| `SDK location not found` | **File > Project Structure > SDK Location** y elige tu Android SDK. |
| Gradle pide otra versión de AGP o de Gradle | Acepta la actualización que sugiere Android Studio. |
| `Unresolved reference` después de sincronizar | **File > Invalidate Caches > Invalidate and Restart**. |
