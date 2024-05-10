package id.ac.istts.ecotong.ui.auth.login

import androidx.navigation.fragment.findNavController
import id.ac.istts.ecotong.databinding.FragmentLoginBinding
import id.ac.istts.ecotong.ui.base.BaseFragment

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    override fun setupUI() {
        with(binding) {

        }
    }

    override fun setupListeners() {
        with(binding) {
            tvToSignUp.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }
        }
    }

    override fun setupObservers() {
    }
}