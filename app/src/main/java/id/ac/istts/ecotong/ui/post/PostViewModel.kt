package id.ac.istts.ecotong.ui.post

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.istts.ecotong.data.repository.PostRepository
import id.ac.istts.ecotong.data.repository.State
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>(null)
    val imageUri = _imageUri as LiveData<Uri?>

    private val _postResponse = MutableLiveData<State<String>>()
    val postResponse = _postResponse as LiveData<State<String>>
    fun setImageUri(uri: Uri) {
        _imageUri.postValue(uri)
    }

    fun addPost(postImage: File, title: String, description: String) {
        viewModelScope.launch {
            postRepository.addPost(postImage, title, description).asFlow().collect {
                _postResponse.postValue(it)
            }
        }
    }
}