package id.ac.istts.ecotong.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetPostsResponse(
    @Json(name = "status") val status: String,
    @Json(name = "data") val data: List<Post>? = null,
    @Json(name = "message") val message: String? = null,
)