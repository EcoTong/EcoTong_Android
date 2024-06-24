package id.ac.istts.ecotong.ui.search

import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.data.repository.State
import id.ac.istts.ecotong.databinding.CommentsDialogBinding
import id.ac.istts.ecotong.databinding.FragmentSavedBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import id.ac.istts.ecotong.ui.home.adapter.CommentAdapter
import id.ac.istts.ecotong.util.gone
import id.ac.istts.ecotong.util.invisible
import id.ac.istts.ecotong.util.toastLong
import id.ac.istts.ecotong.util.visible

@AndroidEntryPoint
class SavedFragment : BaseFragment<FragmentSavedBinding>(FragmentSavedBinding::inflate) {
    private lateinit var commentDialog: BottomSheetDialog
    private var commentViewBinding: CommentsDialogBinding? = null
    private lateinit var commentAdapter: CommentAdapter
    private val viewModel: SavedViewModel by viewModels()
    private lateinit var postAdapter: SavedPostAdapter
    override fun initData() {
        viewModel.getSavedPosts()
    }

    override fun setupUI() {
        postAdapter = SavedPostAdapter(
            ::showComments,
            ::likePost,
            ::unlikePost,
            ::bookmarkPost,
            ::unbookmarkPost
        )
        binding.rvSaved.layoutManager = LinearLayoutManager(context)
        binding.rvSaved.adapter = postAdapter
        commentDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        commentViewBinding = CommentsDialogBinding.inflate(layoutInflater)
        commentDialog.setContentView(commentViewBinding!!.root)
        commentDialog.behavior.halfExpandedRatio = .99f
        commentDialog.behavior.isFitToContents = true
        commentDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        commentDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        commentViewBinding?.rvComments?.layoutManager = LinearLayoutManager(context)
        commentAdapter = CommentAdapter()
        commentViewBinding?.rvComments?.adapter = commentAdapter
        commentDialog.setOnDismissListener {
            commentViewBinding?.editTextText?.text?.clear()
        }
    }

    override fun setupListeners() {

    }

    override fun setupObservers() {
        with(binding) {
            viewModel.posts.observe(viewLifecycleOwner) {
                when (it) {
                    State.Empty -> {
                        progressBar2.gone()
                        rvSaved.gone()
                        tvEmptyPost.visible()
                    }

                    is State.Error -> {
                        progressBar2.gone()
                        tvEmptyPost.gone()
                        rvSaved.gone()
                        requireActivity().toastLong(it.error)
                    }

                    State.Loading -> {
                        tvEmptyPost.gone()
                        rvSaved.gone()
                        progressBar2.visible()
                    }

                    is State.Success -> {
                        progressBar2.gone()
                        tvEmptyPost.gone()
                        rvSaved.visible()
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
                viewModel.addComment(content, id)
                commentViewBinding?.editTextText?.text?.clear()
                val inputMethodManager =
                    ContextCompat.getSystemService(
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