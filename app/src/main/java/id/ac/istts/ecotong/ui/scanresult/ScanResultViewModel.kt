package id.ac.istts.ecotong.ui.scanresult

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.istts.ecotong.data.repository.PostRepository
import id.ac.istts.ecotong.data.repository.State
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanResultViewModel @Inject constructor(private val postRepository: PostRepository) :
    ViewModel() {
    private val _aiResponse = MutableLiveData<State<String>>()
    val aiResponse: MutableLiveData<State<String>> = _aiResponse

    fun getAiResponse(madeFrom: String) {
        viewModelScope.launch {
            postRepository.generateAi(madeFrom).asFlow().collect {
                _aiResponse.value = it
            }
        }
    }
}