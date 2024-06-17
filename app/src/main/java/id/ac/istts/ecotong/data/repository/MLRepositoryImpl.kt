package id.ac.istts.ecotong.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.ac.istts.ecotong.data.remote.response.Prediction
import id.ac.istts.ecotong.data.remote.service.EcotongMLService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class MLRepositoryImpl @Inject constructor(private val mlService: EcotongMLService) : MLRepository {
    override suspend fun predictImage(file: File): LiveData<State<List<Prediction>>> = liveData {
        emit(State.Loading)
        try {
            val imageToPredict = file.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image", file.name, imageToPredict
            )
            val response = mlService.predictImage(multipartBody)
            emit(State.Success(response.predictions))
            Timber.d("xd")
        } catch (e: Exception) {
            Timber.e(e)
            emit(State.Error(e.message.toString()))
        }
    }
}