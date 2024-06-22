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
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _profile = MutableLiveData<State<User>>()
    val profile: LiveData<State<User>> = _profile

    private val _profilePictureResponse = MutableLiveData<State<String>>()
    val profilePictureResponse: LiveData<State<String>> = _profilePictureResponse
    fun getProfile(forceUpdate: Boolean = false) {
        viewModelScope.launch {
            userRepository.getProfile(forceUpdate).asFlow().collect {
                _profile.value = it
            }
        }
    }

    fun updateProfilePicture(file: File) {
        viewModelScope.launch {
            userRepository.updateProfilePicture(file).asFlow().collect {
                _profilePictureResponse.value = it
            }
        }
    }
}