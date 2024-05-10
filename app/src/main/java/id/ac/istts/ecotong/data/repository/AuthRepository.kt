package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import id.ac.istts.ecotong.data.remote.response.User

interface AuthRepository {
    suspend fun register(
        username: String,
        email: String,
        password: String,
        name: String
    ): LiveData<State<User?>>
}