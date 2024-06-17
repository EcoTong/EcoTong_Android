package id.ac.istts.ecotong.ui.profile

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.databinding.FragmentProfileBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import id.ac.istts.ecotong.ui.home.HomeFragmentDirections

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    override fun setupUI() {
    }

    override fun setupListeners() {
        with(binding) {
            goToSettings.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionGlobalSettingsFragment())
            }
        }
    }

    override fun setupObservers() {
    }

}