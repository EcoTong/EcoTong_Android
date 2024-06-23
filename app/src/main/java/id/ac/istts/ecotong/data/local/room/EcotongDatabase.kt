package id.ac.istts.ecotong.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import id.ac.istts.ecotong.data.remote.response.Post
import id.ac.istts.ecotong.data.remote.response.Scan

@Database(entities = [Post::class, Scan::class], version = 2, exportSchema = false)
abstract class EcotongDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao

    abstract fun scanDao(): ScanDao
}