package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.ac.istts.ecotong.data.local.datastore.DataStoreManager
import id.ac.istts.ecotong.data.remote.service.EcotongApiService
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val ecotongApiService: EcotongApiService,
    private val dataStoreManager: DataStoreManager
) : AuthRepository {
    override suspend fun register(
        username: String,
        email: String,
        password: String,
        name: String
    ): LiveData<State<String>> = liveData {
        emit(State.Loading)
        try {
            val response = ecotongApiService.register(username, name, email, password)
            if (response.status == "success") {
                emit(State.Success(response.status))
            } else {
                emit(State.Error(response.message ?: ""))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    override suspend fun login(email: String, password: String): LiveData<State<String>> =
        liveData {
            emit(State.Loading)
            try {
                val response = ecotongApiService.login(email, password)
                if (response.status == "success" && response.token != null) {
                    dataStoreManager.setApiToken(response.token)
                    emit(State.Success(response.status))
                } else {
                    emit(State.Error(response.message ?: ""))
                }
            } catch (e: Exception) {
                emit(State.Error(e.message.toString()))
            }
        }

    override suspend fun oAuthGoogle(idToken: String): LiveData<State<String>> = liveData {
        emit(State.Loading)
        try {
            val response = ecotongApiService.oAuthGoogle(idToken)
            if (response.token != null && response.status != null) {
                dataStoreManager.setApiToken(response.token)
                emit(State.Success(response.status))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    override suspend fun checkToken(): LiveData<State<String>> = liveData {
        emit(State.Loading)
        if (dataStoreManager.ecotongJwtToken.first().isNullOrEmpty()) {
            emit(State.Error("Unauthorized"))
            return@liveData
        } else {
            emit(State.Success("Granted"))
        }
    }
}