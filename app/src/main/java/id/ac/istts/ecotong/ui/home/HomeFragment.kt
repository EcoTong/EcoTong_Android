package id.ac.istts.ecotong.ui.home

import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.data.repository.State
import id.ac.istts.ecotong.databinding.CommentsDialogBinding
import id.ac.istts.ecotong.databinding.FragmentHomeBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import id.ac.istts.ecotong.ui.home.adapter.CommentAdapter
import id.ac.istts.ecotong.ui.home.adapter.HeaderAdapter
import id.ac.istts.ecotong.ui.home.adapter.PostAdapter
import id.ac.istts.ecotong.util.gone
import id.ac.istts.ecotong.util.invisible
import id.ac.istts.ecotong.util.toastLong
import id.ac.istts.ecotong.util.visible

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var headerAdapter: HeaderAdapter
    private lateinit var commentDialog: BottomSheetDialog
    private var commentViewBinding: CommentsDialogBinding? = null
    override fun initData() {
        viewModel.getPosts()
        viewModel.getProfile()
    }

    override fun setupUI() {
        with(binding) {
            postAdapter = PostAdapter(
                ::showComments,
                ::likePost,
                ::unlikePost,
                ::bookmarkPost,
                ::unbookmarkPost
            )
            headerAdapter = HeaderAdapter()
            rvPost.layoutManager = LinearLayoutManager(context)
            val concatAdapter = ConcatAdapter(headerAdapter, postAdapter)
            rvPost.adapter = concatAdapter
            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            val screenHeight = displayMetrics.heightPixels
            val layoutParams = rvPost.layoutParams
            layoutParams.height = screenHeight
            rvPost.layoutParams = layoutParams
            commentDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
            commentViewBinding = CommentsDialogBinding.inflate(layoutInflater)
            commentDialog.setContentView(commentViewBinding!!.root)
            commentDialog.behavior.halfExpandedRatio = 0.5f
            commentDialog.behavior.isFitToContents = true
            commentDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            commentDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            commentViewBinding!!.rvComments.layoutManager = LinearLayoutManager(context)
            commentAdapter = CommentAdapter()
            commentViewBinding?.rvComments?.adapter = commentAdapter
            commentDialog.setOnDismissListener {
                commentViewBinding?.editTextText?.text?.clear()
            }
        }
    }

    override fun setupListeners() {
        with(binding) {
            fabPost.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPostFragment())
            }
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getPosts()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun setupObservers() {
        with(binding) {
            viewModel.profile.observe(viewLifecycleOwner) {
                when (it) {
                    State.Empty -> {}
                    is State.Error -> {}
                    State.Loading -> {}
                    is State.Success -> {
                        headerAdapter.setData(it.data.username, (it.data.credits ?: 0).toString())
                    }
                }
            }
            viewModel.posts.observe(viewLifecycleOwner) {
                when (it) {
                    State.Empty -> {
                        loadingPosts.gone()
                        rvPost.gone()
                        layoutError.gone()
                        tvEmptyPost.visible()
                    }

                    is State.Error -> {
                        loadingPosts.gone()
                        tvEmptyPost.gone()
                        rvPost.gone()
                        layoutError.visible()
                        requireActivity().toastLong(it.error)
                    }

                    State.Loading -> {
                        tvEmptyPost.gone()
                        rvPost.gone()
                        layoutError.gone()
                        loadingPosts.visible()
                    }

                    is State.Success -> {
                        loadingPosts.gone()
                        tvEmptyPost.gone()
                        layoutError.gone()
                        rvPost.visible()
                        postAdapter.submitList(it.data)
                    }
                }
            }
            viewModel.comments.observe(viewLifecycleOwner) {
                when (it) {
                    State.Empty, is State.Error -> {
                        commentViewBinding?.loadComments?.invisible()
                        commentViewBinding?.rvComments?.invisible()
                        commentViewBinding?.tvEmpty?.visible()
                    }

                    State.Loading -> {
                        commentViewBinding?.tvEmpty?.invisible()
                        commentViewBinding?.rvComments?.invisible()
                        commentViewBinding?.loadComments?.visible()
                    }

                    is State.Success -> {
                        commentViewBinding?.tvEmpty?.invisible()
                        commentViewBinding?.loadComments?.invisible()
                        commentViewBinding?.rvComments?.visible()
                        commentAdapter.submitList(it.data)
                    }
                }
            }
        }
    }

    private fun showComments(id: String) {
        commentAdapter.submitList(emptyList())
        viewModel.getComments(id)
        commentViewBinding?.send?.setOnClickListener {
            val content = commentViewBinding?.editTextText?.text.toString()
            if (content.isNotEmpty()) {
                addComment(id, content)
                commentViewBinding?.editTextText?.text?.clear()
                val inputMethodManager =
                    getSystemService(
                        requireContext(),
                        InputMethodManager::class.java
                    ) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    commentViewBinding?.editTextText?.windowToken,
                    0
                )
                viewModel.getComments(id)
            }
        }
        commentDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        commentDialog.show()
        commentViewBinding?.editTextText?.requestFocus()
    }

    private fun addComment(id: String, content: String) {
        viewModel.addComment(content = content, id = id)
    }

    private fun likePost(id: String) {
        viewModel.likePost(id)
    }

    private fun unlikePost(id: String) {
        viewModel.unlikePost(id)
    }

    private fun bookmarkPost(id: String) {
        viewModel.bookmarkPost(id)
    }

    private fun unbookmarkPost(id: String) {
        viewModel.unbookmarkPost(id)
    }
}