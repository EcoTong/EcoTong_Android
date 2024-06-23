package id.ac.istts.ecotong.data.remote.service

import id.ac.istts.ecotong.data.remote.response.AiResponse
import id.ac.istts.ecotong.data.remote.response.AuthResponse
import id.ac.istts.ecotong.data.remote.response.DetailPostResponse
import id.ac.istts.ecotong.data.remote.response.GetPostsResponse
import id.ac.istts.ecotong.data.remote.response.HistoryResponse
import id.ac.istts.ecotong.data.remote.response.PostResponse
import id.ac.istts.ecotong.data.remote.response.ProfileResponse
import id.ac.istts.ecotong.data.remote.response.StatusResponse
import id.ac.istts.ecotong.data.remote.response.TokenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
        @Field("email") email: String, @Field("password") password: String
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

    @GET("/api/users")
    suspend fun getProfile(): ProfileResponse

    @POST("/api/posts/like/{id_post}")
    suspend fun likePost(
        @Path("id_post") postId: String,
    ): StatusResponse

    @DELETE("/api/posts/unlike/{id_post}")
    suspend fun unlikePost(
        @Path("id_post") postId: String,
    ): StatusResponse

    @POST("/api/posts/bookmark/{id_post}")
    suspend fun bookmarkPost(
        @Path("id_post") postId: String,
    ): StatusResponse

    @DELETE("/api/posts/unlike/{id_post}")
    suspend fun unbookmarkPost(
        @Path("id_post") postId: String,
    ): StatusResponse

    @GET("/api/posts/{id}")
    suspend fun getDetailPost(@Path("id") id: String): DetailPostResponse

    @FormUrlEncoded
    @POST("/api/posts/comment/{id_post}")
    suspend fun addComment(
        @Path("id_post") postId: String,
        @Field("content") content: String,
    )

    @GET("/api/ai/generateAi")
    suspend fun generateAi(
        @Query("made_from") madeFrom: String,
        @Query("time1") timeFrom: Int = 3,
        @Query("time2") timeEnd: Int = 5,
    ): AiResponse

    @Multipart
    @PUT("/api/users/profile_picture")
    suspend fun uploadProfilePicture(
        @Part profilePicture: MultipartBody.Part
    ): StatusResponse

    @GET("/api/ai/history")
    suspend fun getHistory(): HistoryResponse
}