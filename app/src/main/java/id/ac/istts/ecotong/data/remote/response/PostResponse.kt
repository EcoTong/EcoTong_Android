package id.ac.istts.ecotong.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostResponse(
    @Json(name = "status") val status: String,
    @Json(name = "data") val data: Post? = null,
    @Json(name = "message") val message: String? = null,
)

@JsonClass(generateAdapter = true)
data class Post(
    @Json(name = "createdAt") val createdAt: String,
    @Json(name = "description") val description: String,
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "picture") val picture: String,
    @Json(name = "username") val username: String,
    @Json(name = "updatedAt") val updatedAt: String
)