package id.ac.istts.ecotong.ui.home

import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.data.repository.State
import id.ac.istts.ecotong.databinding.CommentsDialogBinding
import id.ac.istts.ecotong.databinding.FragmentHomeBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import id.ac.istts.ecotong.ui.home.adapter.ActiveQuestAdapter
import id.ac.istts.ecotong.ui.home.adapter.CommentAdapter
import id.ac.istts.ecotong.ui.home.adapter.PostAdapter
import id.ac.istts.ecotong.util.invisible
import id.ac.istts.ecotong.util.visible

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter
    override fun initData() {
        viewModel.getPosts()
    }

    override fun setupUI() {
        with(binding) {
            postAdapter = PostAdapter(::showComments)
            rvPost.layoutManager = LinearLayoutManager(context)
            rvPost.adapter = postAdapter
            val questAdapter = ActiveQuestAdapter()
            questAdapter.submitList(listOf("A", "B", "C", "D", "E"))
            rvQuest.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvQuest.adapter = questAdapter
            rvPost.isNestedScrollingEnabled = false
            mainSv.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                val contentHeight = mainSv.getChildAt(0).height
                val scrollViewHeight = mainSv.height
                val bottomOfScrollView = contentHeight - scrollViewHeight
                rvPost.isNestedScrollingEnabled = scrollY >= bottomOfScrollView
            }
            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            val screenHeight = displayMetrics.heightPixels
            val layoutParams = rvPost.layoutParams
            layoutParams.height = screenHeight
            rvPost.layoutParams = layoutParams
        }
    }

    override fun setupListeners() {
        with(binding) {
            fabPost.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPostFragment())
            }
            tvShowMore.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionGlobalActiveQuestFragment())
            }
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getPosts()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun setupObservers() {
        with(binding) {
            viewModel.posts.observe(viewLifecycleOwner) {
                when (it) {
                    State.Empty -> {}
                    is State.Error -> {}
                    State.Loading -> {
                        rvPost.invisible()
                        loadingPosts.visible()
                    }

                    is State.Success -> {
                        loadingPosts.invisible()
                        rvPost.visible()
                        postAdapter.submitList(it.data)
                    }
                }
            }
        }
    }

    private fun showComments() {
        val commentDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        val commentView = CommentsDialogBinding.inflate(layoutInflater)
        commentDialog.setContentView(commentView.root)
        commentDialog.behavior.halfExpandedRatio = 0.5f
        commentDialog.behavior.isFitToContents = true
        commentDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        commentDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        commentView.rvComments.layoutManager = LinearLayoutManager(context)
        val adapter2 = CommentAdapter()
        adapter2.submitList(listOf("A", "B", "C"))
        commentView.rvComments.adapter = adapter2
        commentDialog.show()
        commentView.editTextText.requestFocus()
    }
}