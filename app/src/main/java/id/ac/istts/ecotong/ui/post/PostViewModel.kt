package id.ac.istts.ecotong.ui.post

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>(null)
    val imageUri: LiveData<Uri?>
        get() = _imageUri

    fun setImageUri(uri: Uri) {
        _imageUri.postValue(uri)
    }
}