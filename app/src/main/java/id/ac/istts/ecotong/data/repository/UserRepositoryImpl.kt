package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.ac.istts.ecotong.data.local.datastore.SessionManager
import id.ac.istts.ecotong.data.remote.response.User
import id.ac.istts.ecotong.data.remote.service.EcotongApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val ecotongApiService: EcotongApiService,
    private val sessionManager: SessionManager
) :
    UserRepository {
    override suspend fun getProfile(forceUpdate: Boolean): LiveData<State<User>> =
        liveData {
            emit(State.Loading)
            if (!forceUpdate) {
                val localProfile = sessionManager.getProfile()
                if (localProfile != null) {
                    emit(State.Success(localProfile))
                    return@liveData
                }
            }
            try {
                val response = ecotongApiService.getProfile()
                if (response.status == "success" && response.users != null) {
                    sessionManager.saveUser(response.users)
                    emit(State.Success(response.users))
                }
            } catch (e: Exception) {
                emit(State.Error(e.message.toString()))
            }
        }

    override suspend fun updateProfilePicture(file: File): LiveData<State<String>> = liveData {
        emit(State.Loading)
        val postImage = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "fotoprofile", file.name, postImage
        )
        try {
            val response = ecotongApiService.uploadProfilePicture(multipartBody)
            if (response.status == "success") {
                emit(State.Success("Success"))
            } else {
                emit(State.Error("Failed"))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

}