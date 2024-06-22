package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import id.ac.istts.ecotong.data.remote.response.User
import java.io.File

interface UserRepository {
    suspend fun getProfile(forceUpdate: Boolean = false): LiveData<State<User>>
    suspend fun updateProfilePicture(file: File): LiveData<State<String>>

}