package id.ac.istts.ecotong.data.remote.response

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class ProfileResponse(

    @Json(name = "users")
    val users: User? = null,

    @Json(name = "status")
    val status: String,

    @Json(name = "message")
    val message: String? = null
)

@JsonClass(generateAdapter = true)
data class User(

    @Json(name = "name")
    val name: String,

    @Json(name = "profile_picture")
    val profilePicture: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "username")
    val username: String
)
