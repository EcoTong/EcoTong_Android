package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.ac.istts.ecotong.data.remote.service.EcotongApiService
import id.ac.istts.ecotong.util.handleError
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val ecotongApiService: EcotongApiService
) : PostRepository {
    override suspend fun addPost(
        file: File, title: String, description: String
    ): LiveData<State<String>> = liveData {
        emit(State.Loading)
        val titleBody = title.toRequestBody("multipart/form-data".toMediaType())
        val descriptionBody = description.toRequestBody("multipart/form-data".toMediaType())
        val postImage = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "fotopost", file.name, postImage
        )
        try {
            val response = ecotongApiService.addPost(
                titleBody, descriptionBody, multipartBody
            )
            if (response.status == "success") {
                emit(State.Success(response.status))
            } else {
                emit(State.Error(response.message ?: ""))
            }
        } catch (e: Exception) {
            when (e) {
                is HttpException -> emit(State.Error(e.handleError()))
                else -> emit(State.Error("Unexpected error"))
            }
        }
    }

}