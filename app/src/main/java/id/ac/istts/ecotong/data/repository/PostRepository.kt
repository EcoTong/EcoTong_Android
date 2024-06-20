package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import id.ac.istts.ecotong.data.remote.response.Comments
import id.ac.istts.ecotong.data.remote.response.Post
import kotlinx.coroutines.flow.Flow
import java.io.File

interface PostRepository {
    suspend fun addPost(
        file: File, title: String, description: String
    ): LiveData<State<String>>

    suspend fun getPosts(): LiveData<State<List<Post>>>
    suspend fun getComments(id: String): LiveData<State<List<Comments>>>
    suspend fun addComment(content: String, id: String)
    suspend fun likePost(id: String)
    suspend fun unlikePost(id: String)
    suspend fun bookmarkPost(id: String)
    suspend fun unbookmarkPost(id: String)
    suspend fun generateAi(madeFrom: String): LiveData<State<String>>
}