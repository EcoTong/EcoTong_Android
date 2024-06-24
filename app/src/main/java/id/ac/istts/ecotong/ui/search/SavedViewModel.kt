package id.ac.istts.ecotong.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.istts.ecotong.data.remote.response.Comments
import id.ac.istts.ecotong.data.remote.response.Post
import id.ac.istts.ecotong.data.remote.response.SavedPost
import id.ac.istts.ecotong.data.repository.PostRepository
import id.ac.istts.ecotong.data.repository.State
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _posts = MutableLiveData<State<List<SavedPost>>>()
    val posts: LiveData<State<List<SavedPost>>> = _posts

    private val _comments = MutableLiveData<State<List<Comments>>>()
    val comments: LiveData<State<List<Comments>>> = _comments

    fun getSavedPosts() {
        viewModelScope.launch {
            postRepository.getSavedPosts().asFlow().collect {
                _posts.postValue(it)
            }
        }
    }

    fun likePost(id: String) {
        viewModelScope.launch {
            postRepository.likePost(id)
        }
    }

    fun unlikePost(id: String) {
        viewModelScope.launch {
            postRepository.unlikePost(id)
        }
    }

    fun bookmarkPost(id: String) {
        viewModelScope.launch {
            postRepository.bookmarkPost(id)
        }
    }

    fun unbookmarkPost(id: String) {
        viewModelScope.launch {
            postRepository.unbookmarkPost(id)
        }
    }

    fun getComments(id: String) {
        viewModelScope.launch {
            postRepository.getComments(id).asFlow().collect {
                _comments.postValue(it)
            }
        }
    }

    fun addComment(content: String, id: String) {
        viewModelScope.launch {
            postRepository.addComment(content = content, id = id)
        }
    }
}