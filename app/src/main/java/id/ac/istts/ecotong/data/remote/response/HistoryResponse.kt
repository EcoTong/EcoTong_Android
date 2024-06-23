package id.ac.istts.ecotong.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class HistoryResponse(

    @Json(name = "data")
    val data: List<Scan>? = null,

    @Json(name = "status")
    val status: String
)

@Entity(tableName = "scans")
@JsonClass(generateAdapter = true)
data class Scan(

    @Json(name = "createdAt")
    val createdAt: String,

    @Json(name = "instruction")
    val instruction: String,

    @PrimaryKey
    @Json(name = "id")
    val id: Int,

    @Json(name = "made_from")
    val madeFrom: String,

    @Json(name = "username")
    val username: String,

    @Json(name = "updatedAt")
    val updatedAt: String
)
