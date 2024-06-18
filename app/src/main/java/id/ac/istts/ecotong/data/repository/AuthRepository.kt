package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData

interface AuthRepository {
    suspend fun register(
        username: String, email: String, password: String, name: String
    ): LiveData<State<String>>

    suspend fun login(
        email: String,
        password: String,
    ): LiveData<State<String>>

    suspend fun oAuthGoogle(idToken: String): LiveData<State<String>>
    suspend fun checkToken(): LiveData<State<String>>

    suspend fun logout()
}