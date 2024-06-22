package id.ac.istts.ecotong.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import id.ac.istts.ecotong.data.remote.response.Post

@Database(entities = [Post::class], version = 1, exportSchema = false)
abstract class EcotongDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}