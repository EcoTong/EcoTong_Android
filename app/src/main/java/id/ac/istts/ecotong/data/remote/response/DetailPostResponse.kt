package id.ac.istts.ecotong.data.remote.response

import com.squareup.moshi.Json

data class DetailPostResponse(
    @Json(name = "data") val detailPost: DetailPost? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "status") val status: String
)

data class DetailPost(

    @Json(name = "createdAt") val createdAt: String? = null,

    @Json(name = "comments") val comments: List<Comments>? = null,

    @Json(name = "description") val description: String? = null,

    @Json(name = "id") val id: String? = null,

    @Json(name = "category") val category: Any? = null,

    @Json(name = "title") val title: String? = null,

    @Json(name = "picture") val picture: String? = null,

    @Json(name = "username") val username: String? = null,

    @Json(name = "updatedAt") val updatedAt: String? = null
)

data class Comments(

    @Json(name = "createdAt") val createdAt: String,

    @Json(name = "post_id") val postId: String,

    @Json(name = "id") val id: String? = null,

    @Json(name = "content") val content: String? = null,

    @Json(name = "username") val username: String? = null,

    @Json(name = "updatedAt") val updatedAt: String? = null
)