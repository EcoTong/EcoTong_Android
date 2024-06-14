package id.ac.istts.ecotong.util

import com.squareup.moshi.Moshi
import id.ac.istts.ecotong.data.remote.response.ErrorResponseJsonAdapter
import retrofit2.HttpException

private val moshi = Moshi.Builder().build()
fun HttpException.handleError(): String {
    val adapter = ErrorResponseJsonAdapter(moshi)
    val jsonInString = response()?.errorBody()?.string()
    val errorBody = jsonInString?.let { adapter.fromJson(it) }
    return errorBody?.message ?: ""
}