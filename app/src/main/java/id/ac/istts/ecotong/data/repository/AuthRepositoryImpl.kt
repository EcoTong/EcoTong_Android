package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.ac.istts.ecotong.data.local.datastore.SessionManager
import id.ac.istts.ecotong.data.remote.service.EcotongApiService
import id.ac.istts.ecotong.util.handleError
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val ecotongApiService: EcotongApiService,
    private val sessionManager: SessionManager
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
            when (e) {
                is HttpException -> emit(State.Error(e.handleError()))
                else -> emit(State.Error("Unexpected error"))
            }
        }
    }

    override suspend fun login(email: String, password: String): LiveData<State<String>> =
        liveData {
            emit(State.Loading)
            try {
                val response = ecotongApiService.login(email, password)
                if (response.status == "success" && response.token != null) {
                    sessionManager.setApiToken(response.token)
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

    override suspend fun oAuthGoogle(idToken: String): LiveData<State<String>> = liveData {
        emit(State.Loading)
        try {
            val response = ecotongApiService.oAuthGoogle(idToken)
            if (response.token != null && response.status != null) {
                sessionManager.setApiToken(response.token)
                emit(State.Success(response.status))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    override suspend fun checkToken(): LiveData<State<String>> = liveData {
        emit(State.Loading)
        if (sessionManager.ecotongJwtToken.first().isNullOrEmpty()) {
            emit(State.Error("Unauthorized"))
            return@liveData
        } else {
            emit(State.Success("Granted"))
        }
    }

    override suspend fun logout() {
        sessionManager.removeToken()
    }
}