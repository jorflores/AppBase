package mx.tec.appbase.data.remote

import kotlinx.serialization.Serializable

// Lo que mandamos a POST /login
@Serializable
data class LoginRequest(
    val correo: String,
    val password: String,
)

// Lo que responde POST /login
@Serializable
data class LoginResponse(
    val token: String,
)

// Lo que responde GET /perfil (la API nunca regresa el password)
@Serializable
data class Usuario(
    val id: Int,
    val correo: String,
    val rol: String,
)
