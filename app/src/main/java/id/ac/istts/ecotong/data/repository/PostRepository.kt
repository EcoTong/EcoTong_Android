package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import id.ac.istts.ecotong.data.remote.response.Post
import java.io.File

interface PostRepository {
    suspend fun addPost(
        file: File,
        title: String,
        description: String
    ): LiveData<State<String>>

    suspend fun getPosts(): LiveData<State<List<Post>>>
}