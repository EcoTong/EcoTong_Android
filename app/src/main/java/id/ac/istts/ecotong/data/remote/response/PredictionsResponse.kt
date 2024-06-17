package id.ac.istts.ecotong.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PredictionsResponse(
    @Json(name = "predictions") val predictions: List<Prediction>
)

@JsonClass(generateAdapter = true)
data class Prediction(
    @Json(name = "metal") val metal: Float? = null,
    @Json(name = "glass") val glass: Float? = null,
    @Json(name = "plastic") val plastic: Float? = null,
    @Json(name = "paper") val paper: Float? = null,
    @Json(name = "biodegradable") val biodegradable: Float? = null,
    @Json(name = "cardboard") val cardboard: Float? = null
)

