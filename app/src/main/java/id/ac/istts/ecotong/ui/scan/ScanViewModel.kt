package id.ac.istts.ecotong.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.istts.ecotong.data.remote.response.Prediction
import id.ac.istts.ecotong.data.remote.response.PredictionsResponse
import id.ac.istts.ecotong.data.repository.MLRepository
import id.ac.istts.ecotong.data.repository.State
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val mlRepository: MLRepository) : ViewModel() {
    private val _predictionResponse = MutableLiveData<State<List<Prediction>>>()
    val predictionResponse = _predictionResponse as LiveData<State<List<Prediction>>>
    fun predictImage(image: File) {
        viewModelScope.launch {
            mlRepository.predictImage(file = image).asFlow().collect {
                _predictionResponse.postValue(it)
            }
        }
    }
}