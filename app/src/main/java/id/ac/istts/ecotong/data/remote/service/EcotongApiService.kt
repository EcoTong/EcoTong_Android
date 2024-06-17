package id.ac.istts.ecotong.data.remote.service

import id.ac.istts.ecotong.data.remote.response.AuthResponse
import id.ac.istts.ecotong.data.remote.response.GetPostsResponse
import id.ac.istts.ecotong.data.remote.response.PostResponse
import id.ac.istts.ecotong.data.remote.response.TokenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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

    @Multipart
    @POST("/api/posts/tambahpost")
    suspend fun addPost(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part fotopost: MultipartBody.Part
    ): PostResponse

    @GET("/api/posts")
    suspend fun getPosts(): GetPostsResponse
}