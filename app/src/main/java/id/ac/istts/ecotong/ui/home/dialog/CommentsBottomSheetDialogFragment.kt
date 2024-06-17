package id.ac.istts.ecotong.ui.home.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.databinding.CommentsDialogBinding
import id.ac.istts.ecotong.ui.home.adapter.CommentAdapter

class CommentsBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: CommentsDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = CommentsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = CommentsDialogBinding.bind(view)
        binding.rvComments.layoutManager = LinearLayoutManager(context)
        val adapter = CommentAdapter()
        adapter.submitList(listOf("Comment A", "Comment B", "Comment C", "Comment D", "Comment E"))
        binding.rvComments.adapter = adapter

        val behavior = getBehavior()
        behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                // Handle state changes
            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
                // Handle slide events
            }
        })
    }

    private fun getBehavior(): BottomSheetBehavior<*> {
        val dialog = dialog
        val behavior = BottomSheetBehavior.from(binding.root)
        //Set to screen size
        behavior.peekHeight = ViewGroup.LayoutParams.MATCH_PARENT
        behavior.isFitToContents = false
        behavior.halfExpandedRatio = 0.5f
        return behavior
    }

    companion object {
        fun show(activity: FragmentActivity) {
            CommentsBottomSheetDialogFragment().show(
                activity.supportFragmentManager, "CommentsBottomSheetDialogFragment"
            )
        }
    }
}