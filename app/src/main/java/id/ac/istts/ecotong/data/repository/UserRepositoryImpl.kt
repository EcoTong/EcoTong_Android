package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.ac.istts.ecotong.data.remote.response.User
import id.ac.istts.ecotong.data.remote.service.EcotongApiService
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val ecotongApiService: EcotongApiService) :
    UserRepository {
    override suspend fun getProfile(): LiveData<State<User>> = liveData {
        emit(State.Loading)
        try {
            val response = ecotongApiService.getProfile()
            if (response.status == "success" && response.users != null) {
                emit(State.Success(response.users))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

}