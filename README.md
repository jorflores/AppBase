# AppBase: cascarón de navegación para tu proyecto

App de punto de partida en Kotlin + Jetpack Compose con:

- **Login real contra la API** ([api-usuarios-2026](https://api-usuarios-2026.onrender.com/)): `POST /login` regresa un token JWT y con él se pide `GET /perfil`.
- **Sesión guardada con DataStore:** el token se guarda en el teléfono, así que al volver a abrir la app entras directo sin iniciar sesión otra vez.
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

Todas están en `gradle/libs.versions.toml`: Kotlin 2.4.10, Navigation 2.9.8, DataStore 1.3.0, Compose BOM, compileSdk/targetSdk 36 y minSdk 26. El permiso de Internet ya viene en el `AndroidManifest.xml`.

## 4. Login con la API

- La URL de la API está en `data/remote/RetrofitClient.kt` (`BASE_URL`).
- Usuario de prueba: el admin que crea el seed de la API (por defecto `admin@mail.com` / `1234`).
- Flujo: `LoginScreen` → `SesionViewModel.iniciarSesion()` → `AuthRepository` → `POST /login` (token) → `GET /perfil` con `Authorization: Bearer <token>` → `PrefsRepository.guardarToken()`. Si todo sale bien, la app entra y **Perfil** muestra el correo y el rol.
- Errores: 401 muestra "Correo o contraseña incorrectos"; sin red, "No se pudo conectar con el servidor".
- **Cerrar sesión** (menú lateral) borra el token de memoria y de DataStore, y regresa al login.

## 5. Sesión guardada con DataStore

- Al iniciar sesión, el token se guarda en DataStore, en el archivo `sesion_prefs.preferences_pb` dentro del teléfono (puedes verlo en **Device Explorer**: `/data/data/mx.tec.appbase/files/datastore/`).
- Al abrir la app, `SesionViewModel` lee el token guardado. Si existe, la app abre directo en la parte principal; si no, en el login.
- Con el token guardado, el perfil se pide a la API en segundo plano (por eso **Perfil** puede decir "Cargando perfil…" mientras Render despierta).
- Si la API responde **401** (token expirado o inválido), la sesión se cierra sola y la app regresa al login. Si no hay internet, la sesión se conserva.
- **Para probarlo:** inicia sesión, cierra la app por completo (desde recientes) y vuelve a abrirla. Debes entrar sin pasar por el login.
- Archivos: `data/local/DataStoreModule.kt` (la instancia), `data/local/PrefKeys.kt` (las claves) y `data/repository/PrefsRepository.kt` (leer, guardar y borrar el token).
- Para guardar otro dato (por ejemplo, tema oscuro): agrega su clave en `PrefKeys` y sus funciones en `PrefsRepository`.
- El plan gratuito de Render se duerme: el primer login del día puede tardar cerca de un minuto (los timeouts ya están en 60 s).
- Para ver las peticiones en Logcat, filtra con `tag~:okhttp`.

## 6. Estructura

```
mx.tec.appbase
├── MainActivity.kt              Arranca la app
├── navigation
│   ├── Rutas.kt                 Rutas y pantallas (con título e ícono) + listas de menús
│   ├── AppNavigation.kt         Nivel 1: Login ↔ Principal (abre en Principal si hay token)
│   └── PrincipalNavHost.kt      Nivel 2: las 10 pantallas internas
├── data
│   ├── remote
│   │   ├── ApiService.kt        Rutas de la API (login, perfil)
│   │   ├── Modelos.kt           LoginRequest, LoginResponse, Usuario
│   │   └── RetrofitClient.kt    URL base, JSON y Logcat
│   ├── local
│   │   ├── DataStoreModule.kt   La instancia de DataStore (archivo sesion_prefs)
│   │   └── PrefKeys.kt          Claves de lo que se guarda (token)
│   └── repository
│       ├── AuthRepository.kt    iniciarSesion() y obtenerPerfil()
│       └── PrefsRepository.kt   Leer, guardar y borrar el token en DataStore
└── ui
    ├── components
    │   ├── BarraSuperior.kt     TopAppBar con botón ☰
    │   ├── BarraInferior.kt     Bottom navigation
    │   ├── MenuLateral.kt       Navigation drawer
    │   └── PantallaEjemplo.kt   Contenido de relleno de cada pantalla
    ├── screens
    │   ├── LoginScreen.kt       Llama al SesionViewModel y muestra errores
    │   ├── PrincipalScreen.kt   El cascarón: drawer + barras + NavHost
    │   ├── inferior/            Inicio, Buscar, Notificaciones, Favoritos, Perfil
    │   └── lateral/             Calendario, Mensajes, Reportes, Configuracion, AcercaDe
    ├── viewmodel
    │   └── SesionViewModel.kt   Sesión compartida por toda la app; restaura el token al abrir
    └── theme                    Colores, tema y tipografía
```

## 7. Cómo agregar y cambiar pantallas a tu proyecto

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
