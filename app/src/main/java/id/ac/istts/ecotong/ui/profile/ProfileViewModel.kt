package id.ac.istts.ecotong.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.istts.ecotong.data.remote.response.User
import id.ac.istts.ecotong.data.repository.State
import id.ac.istts.ecotong.data.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val _profile = MutableLiveData<State<User>>()
    val profile: LiveData<State<User>> = _profile
    fun getProfile() {
        viewModelScope.launch {
            userRepository.getProfile().asFlow().collect {
                _profile.value = it
            }
        }
    }
}