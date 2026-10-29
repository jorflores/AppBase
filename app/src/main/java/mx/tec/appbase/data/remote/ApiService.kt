package mx.tec.appbase.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// Las rutas de la API que usa la app.
interface ApiService {

    // Pública: correo + password -> token
    @POST("login")
    suspend fun login(@Body datos: LoginRequest): LoginResponse

    // Protegida: hay que mandar "Bearer <token>"
    @GET("perfil")
    suspend fun perfil(@Header("Authorization") auth: String): Usuario
}
