package id.ac.istts.ecotong.ui.auth.login

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
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _oAuthResponse = MutableLiveData<State<String>>()
    val oAuthResponse = _oAuthResponse as LiveData<State<String>>
    private val _loginResponse = MutableLiveData<State<String>>()
    val loginResponse = _loginResponse as LiveData<State<String>>

    fun oAuthGoogle(idToken: String) {
        viewModelScope.launch {
            authRepository.oAuthGoogle(idToken).asFlow().collect {
                _oAuthResponse.postValue(it)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).asFlow().collect {
                _loginResponse.postValue(it)
            }
        }
    }
}