package id.ac.istts.ecotong.data.remote.response

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class TokenResponse(

    @Json(name = "status") val status: String? = null,

    @Json(name = "token") val token: String? = null
)
