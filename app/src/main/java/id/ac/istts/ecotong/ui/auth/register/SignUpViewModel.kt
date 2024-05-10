package id.ac.istts.ecotong.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.istts.ecotong.data.remote.response.User
import id.ac.istts.ecotong.data.repository.AuthRepository
import id.ac.istts.ecotong.data.repository.State
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _registerResponse = MutableLiveData<State<User?>>()
    val registerResponse = _registerResponse as LiveData<State<User?>>
    fun register(username: String, name: String, password: String, email: String) {
        viewModelScope.launch {
            authRepository.register(username, email, password, name).asFlow().collect {
                _registerResponse.postValue(it)
            }
        }
    }

}