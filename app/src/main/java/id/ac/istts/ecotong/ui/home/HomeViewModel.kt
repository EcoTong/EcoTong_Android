package id.ac.istts.ecotong.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.istts.ecotong.data.remote.response.Post
import id.ac.istts.ecotong.data.repository.PostRepository
import id.ac.istts.ecotong.data.repository.State
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {
    private val _posts = MutableLiveData<State<List<Post>>>()
    val posts: LiveData<State<List<Post>>> = _posts
    fun getPosts() {
        viewModelScope.launch {
            postRepository.getPosts().asFlow().collect {
                _posts.value = it
            }
        }
    }
}