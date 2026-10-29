package mx.tec.appbase.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // URL de la API en Render (siempre con "/" al final).
    // Para probar la API local desde el emulador: "http://10.0.2.2:3000/"
    // (y agrega android:usesCleartextTraffic="true" en el AndroidManifest, solo en desarrollo).
    private const val BASE_URL = "https://api-usuarios-2026.onrender.com/"

    // Si la API agrega campos nuevos, la app no truena.
    private val json = Json { ignoreUnknownKeys = true }

    // Imprime en Logcat cada petición y respuesta. Filtro: tag~:okhttp
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // El plan gratuito de Render se duerme y tarda cerca de un minuto en despertar,
    // por eso los timeouts son más largos que los 10 s por defecto.
    private val cliente = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cliente)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}
