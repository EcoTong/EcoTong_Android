package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import id.ac.istts.ecotong.data.remote.response.User

interface UserRepository {
    suspend fun getProfile(): LiveData<State<User>>
}