package id.ac.istts.ecotong.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ac.istts.ecotong.data.remote.response.Scan

@Dao
interface ScanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addScans(scans: List<Scan>)

    @Query("SELECT * FROM scans ORDER BY createdAt DESC")
    suspend fun getScans(): List<Scan>

    @Query("DELETE FROM scans")
    suspend fun deleteScans()
}