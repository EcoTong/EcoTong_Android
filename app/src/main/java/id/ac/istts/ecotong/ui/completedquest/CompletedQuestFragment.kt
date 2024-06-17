package id.ac.istts.ecotong.ui.completedquest

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import id.ac.istts.ecotong.databinding.FragmentCompletedQuestBinding
import id.ac.istts.ecotong.ui.base.BaseFragment

class CompletedQuestFragment: BaseFragment<FragmentCompletedQuestBinding>(FragmentCompletedQuestBinding::inflate) {
    override fun setupUI() {
    }
    override fun setupListeners() {
        with(binding) {
            toolbarPost.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
    override fun setupObservers() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
        setupObservers()
    }
}