package id.ac.istts.ecotong.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.istts.ecotong.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    suspend fun logout() {
        authRepository.logout()
    }
}