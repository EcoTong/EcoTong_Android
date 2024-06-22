package id.ac.istts.ecotong.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostResponse(
    @Json(name = "status") val status: String,
    @Json(name = "data") val data: Post? = null,
    @Json(name = "message") val message: String? = null,
)

@Entity(tableName = "posts")
@JsonClass(generateAdapter = true)
data class Post(

    @Json(name = "createdAt")
    val createdAt: String,

    @Json(name = "comments")
    val comments: Int? = null,

    @Json(name = "description")
    val description: String,

    @PrimaryKey
    @Json(name = "id")
    val id: String,

    @Json(name = "title")
    val title: String,

    @Json(name = "picture")
    val picture: String,

    @Json(name = "liked")
    val liked: Boolean? = null,

    @Json(name = "bookmarked")
    val bookmarked: Boolean? = null,

    @Json(name = "username")
    val username: String,

    @Json(name = "updatedAt")
    val updatedAt: String,

    @Json(name = "likes")
    val likes: Int? = null
)
