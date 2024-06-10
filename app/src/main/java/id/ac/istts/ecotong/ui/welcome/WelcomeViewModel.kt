package id.ac.istts.ecotong.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.istts.ecotong.data.repository.AuthRepository
import id.ac.istts.ecotong.data.repository.State
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _checkTokenResponse = MutableLiveData<State<String>>()
    val checkTokenResponse = _checkTokenResponse as LiveData<State<String>>
    fun checkToken() {
        viewModelScope.launch {
            authRepository.checkToken().asFlow().collect {
                _checkTokenResponse.postValue(it)
            }
        }
    }
}