package id.ac.istts.ecotong.ui.welcome

import androidx.navigation.fragment.findNavController
import id.ac.istts.ecotong.databinding.FragmentToLoginBinding
import id.ac.istts.ecotong.ui.base.BaseFragment

class ToLoginFragment : BaseFragment<FragmentToLoginBinding>(FragmentToLoginBinding::inflate) {
    override fun setupUI() {}

    override fun setupListeners() {
        with(binding) {
            btnLogin.setOnClickListener {
                findNavController().navigate(ToLoginFragmentDirections.actionToLoginFragmentToLoginFragment())
            }
            tvToSignUp.setOnClickListener {
                findNavController().navigate(ToLoginFragmentDirections.actionToLoginFragmentToSignUpFragment())
            }
        }
    }

    override fun setupObservers() {
    }
}