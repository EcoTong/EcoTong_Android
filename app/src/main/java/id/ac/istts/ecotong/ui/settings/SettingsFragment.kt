package id.ac.istts.ecotong.ui.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import id.ac.istts.ecotong.databinding.FragmentSettingsBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import id.ac.istts.ecotong.ui.home.HomeFragmentDirections

class SettingsFragment: BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    override fun setupUI() {

    }
    override fun setupListeners() {
        with(binding) {
            toolbarPost.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            goToAppInfo.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionGlobalAppInfoFragment())
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