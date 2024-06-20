package id.ac.istts.ecotong.data.remote.response

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class AiResponse(

	@Json(name="message")
	val message: String? = null,

	@Json(name="status")
	val status: String
)
