package id.ac.istts.ecotong.data.remote.service

import id.ac.istts.ecotong.data.remote.response.AuthResponse
import id.ac.istts.ecotong.data.remote.response.TokenResponse
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

    @FormUrlEncoded
    @POST("/api/users/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("/api/auth/google")
    suspend fun oAuthGoogle(
        @Field("id_token") idToken: String
    ): TokenResponse
}