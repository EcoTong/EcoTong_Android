package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.ac.istts.ecotong.data.remote.response.Comments
import id.ac.istts.ecotong.data.remote.response.Post
import id.ac.istts.ecotong.data.remote.service.EcotongApiService
import id.ac.istts.ecotong.util.handleError
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import timber.log.Timber
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

    override suspend fun getPosts(): LiveData<State<List<Post>>> = liveData {
        emit(State.Loading)
        try {
            val response = ecotongApiService.getPosts()
            if (response.status == "success" && response.data != null) {
                if (response.data.isEmpty()) emit(State.Empty)
                emit(State.Success(response.data))
            } else {
                emit(State.Error(response.message ?: ""))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    override suspend fun getComments(id: String): LiveData<State<List<Comments>>> = liveData {
        emit(State.Loading)
        try {
            val response = ecotongApiService.getDetailPost(id)
            if (response.status == "success" && response.detailPost != null && response.detailPost.comments != null) {
                if (response.detailPost.comments.isEmpty()) {
                    emit(State.Empty)
                } else {
                    emit(State.Success(response.detailPost.comments))
                }
            } else {
                emit(State.Error(response.message ?: ""))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    override suspend fun addComment(content: String, id: String) {
        try {
            ecotongApiService.addComment(id, content)
        } catch (e: Exception) {
            Timber.e(e.message.toString())
        }
    }


    override suspend fun likePost(id: String) {
        try {
            ecotongApiService.likePost(id)
        } catch (e: Exception) {
            Timber.e(e.message.toString())
        }
    }

    override suspend fun unlikePost(id: String) {
        try {
            ecotongApiService.unlikePost(id)
        } catch (e: Exception) {
            Timber.e(e.message.toString())
        }
    }

    override suspend fun bookmarkPost(id: String) {
        try {
            ecotongApiService.bookmarkPost(id)
        } catch (e: Exception) {
            Timber.e(e.message.toString())
        }
    }

    override suspend fun unbookmarkPost(id: String) {
        try {
            ecotongApiService.unbookmarkPost(id)
        } catch (e: Exception) {
            Timber.e(e.message.toString())
        }
    }

}