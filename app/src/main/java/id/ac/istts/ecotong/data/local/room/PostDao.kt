package id.ac.istts.ecotong.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ac.istts.ecotong.data.remote.response.Post
@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPosts(posts: List<Post>)

    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    suspend fun getPosts(): List<Post>

    @Query("DELETE FROM posts")
    suspend fun deletePosts()
}