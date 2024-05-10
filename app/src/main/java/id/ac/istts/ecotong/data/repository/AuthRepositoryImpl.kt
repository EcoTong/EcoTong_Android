package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.ac.istts.ecotong.data.remote.response.User
import id.ac.istts.ecotong.data.remote.service.EcotongApiService
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val ecotongApiService: EcotongApiService,
) : AuthRepository {
    override suspend fun register(
        username: String,
        email: String,
        password: String,
        name: String
    ): LiveData<State<User?>> = liveData {
        emit(State.Loading)
        try {
            val response = ecotongApiService.register(username, name, email, password)
            if (response.status == "success") {
                emit(State.Success(response.user))
            } else {
                emit(State.Error(response.message ?: ""))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }
}