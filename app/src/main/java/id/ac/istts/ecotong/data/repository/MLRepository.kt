package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import id.ac.istts.ecotong.data.remote.response.Prediction
import id.ac.istts.ecotong.data.remote.response.PredictionsResponse
import java.io.File

interface MLRepository {
    suspend fun predictImage(
        file: File,
    ): LiveData<State<List<Prediction>>>
}