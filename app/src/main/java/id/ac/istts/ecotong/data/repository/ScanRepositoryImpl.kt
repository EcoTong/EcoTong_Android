package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.ac.istts.ecotong.data.local.room.ScanDao
import id.ac.istts.ecotong.data.remote.response.Scan
import id.ac.istts.ecotong.data.remote.service.EcotongApiService
import javax.inject.Inject

class ScanRepositoryImpl @Inject constructor(
    private val ecotongApiService: EcotongApiService,
    private val scanDao: ScanDao
) : ScanRepository {
    override suspend fun getScans(): LiveData<State<List<Scan>>> = liveData {
        emit(State.Loading)
        try {
            val response = ecotongApiService.getHistory()
            if (response.status == "success" && response.data != null) {
                scanDao.deleteScans()
                scanDao.addScans(response.data)
                val scans = scanDao.getScans()
                emit(State.Success(scans))
            } else {
                val localScans = scanDao.getScans()
                if (localScans.isEmpty()) {
                    emit(State.Empty)
                } else {
                    emit(State.Success(localScans))
                }
            }
        } catch (e: Exception) {
            val localScans = scanDao.getScans()
            if (localScans.isEmpty()) {
                emit(State.Empty)
            } else {
                emit(State.Success(localScans))
            }
        }
    }
}