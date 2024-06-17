package id.ac.istts.ecotong.ui.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import id.ac.istts.ecotong.databinding.FragmentAppInfoBinding
import id.ac.istts.ecotong.ui.base.BaseFragment

class AppInfoFragment: BaseFragment<FragmentAppInfoBinding>(FragmentAppInfoBinding::inflate) {
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