package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import java.io.File

interface PostRepository {
    suspend fun addPost(
        file: File,
        title: String,
        description: String
    ): LiveData<State<String>>
}