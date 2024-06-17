package id.ac.istts.ecotong.data.remote.service

import id.ac.istts.ecotong.data.remote.response.PredictionsResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface EcotongMLService {
    @Multipart
    @POST("/ecotongmodel")
    suspend fun predictImage(
        @Part image: MultipartBody.Part
    ): PredictionsResponse
}