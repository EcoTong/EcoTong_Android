package id.ac.istts.ecotong.data.remote.service

import id.ac.istts.ecotong.data.remote.response.AuthResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface EcotongApiService {
    @FormUrlEncoded
    @POST("/api/users/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse


}