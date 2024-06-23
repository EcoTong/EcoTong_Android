package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import id.ac.istts.ecotong.data.remote.response.Scan

interface ScanRepository {
    suspend fun getScans(): LiveData<State<List<Scan>>>
}