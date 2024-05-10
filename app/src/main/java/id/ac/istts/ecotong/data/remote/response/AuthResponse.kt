package id.ac.istts.ecotong.data.remote.response

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class AuthResponse(

    @Json(name = "data")
    val user: User? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "status")
    val status: String
)

@JsonClass(generateAdapter = true)
data class User(

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "password")
    val password: String? = null,

    @Json(name = "credits")
    val credits: Int? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "profile_picture")
    val profilePicture: String? = null,

    @Json(name = "email")
    val email: String? = null,

    @Json(name = "username")
    val username: String? = null,

    @Json(name = "updatedAt")
    val updatedAt: String? = null
)
