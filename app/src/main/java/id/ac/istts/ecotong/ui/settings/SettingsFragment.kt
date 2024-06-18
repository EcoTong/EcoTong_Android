package id.ac.istts.ecotong.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.databinding.FragmentSettingsBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    private val viewModel: SettingsViewModel by viewModels()
    override fun setupUI() {

    }

    override fun setupListeners() {
        with(binding) {
            toolbarPost.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            goToAppInfo.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToAppInfoFragment())
            }
            tvLogout.setOnClickListener {
                logout()
            }
            ivLogout.setOnClickListener {
                logout()
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

    private fun logout() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.logout()
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToToLoginFragment())
        }
    }
}