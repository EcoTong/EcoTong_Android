package id.ac.istts.ecotong.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.istts.ecotong.data.remote.response.Scan
import id.ac.istts.ecotong.data.repository.ScanRepository
import id.ac.istts.ecotong.data.repository.State
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val scanRepository: ScanRepository
) : ViewModel() {
    private val _historyResponse = MutableLiveData<State<List<Scan>>>()
    val historyResponse: LiveData<State<List<Scan>>> = _historyResponse

    fun getHistory() {
        viewModelScope.launch {
            scanRepository.getScans().asFlow().collect {
                _historyResponse.value = it
            }
        }
    }
}