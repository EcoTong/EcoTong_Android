package id.ac.istts.ecotong.ui.auth.register

import androidx.core.widget.doOnTextChanged
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.BuildConfig
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.data.repository.State
import id.ac.istts.ecotong.databinding.FragmentSignUpBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import id.ac.istts.ecotong.util.invisible
import id.ac.istts.ecotong.util.toastLong
import id.ac.istts.ecotong.util.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {
    private val viewModel: SignUpViewModel by viewModels()
    override fun setupUI() {
        with(binding) {
        }
    }

    override fun setupListeners() {
        with(binding) {
            tvToLogin.setOnClickListener {
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
            }
            etEmail.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrEmpty()) {
                    etEmail.error =
                        getString(R.string.please_fill_out_this_field)
                    return@doOnTextChanged
                }
                if (!text.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) {
                    etEmail.error = getString(R.string.invalid_email_format)
                    return@doOnTextChanged
                }
            }
            etName.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrEmpty()) {
                    etName.error =
                        getString(R.string.please_fill_out_this_field)
                    return@doOnTextChanged
                }
            }
            etUsername.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrEmpty()) {
                    etUsername.error =
                        getString(R.string.please_fill_out_this_field)
                    return@doOnTextChanged
                }
            }
            etPassword.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrEmpty() || text.length < 8) {
                    etPassword.error = getString(R.string.minimum_password)
                    return@doOnTextChanged
                }
                if (text.toString() != etConfirmPassword.text.toString()) {
                    etConfirmPassword.error =
                        getString(R.string.confirmation_password_does_not_match)
                }
            }
            etConfirmPassword.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrEmpty()) {
                    etConfirmPassword.error =
                        getString(R.string.please_fill_out_this_field)
                    return@doOnTextChanged
                }
                if (text.toString() != etPassword.text.toString()) {
                    etConfirmPassword.error =
                        getString(R.string.confirmation_password_does_not_match)
                }
            }
            btnRegister.setOnClickListener {
                val inputFields = listOf(etEmail, etUsername, etName, etPassword, etConfirmPassword)
                inputFields.forEach {
                    if (it.text.isEmpty()) {
                        it.error = getString(R.string.please_fill_out_this_field)
                        return@setOnClickListener
                    }
                }
                inputFields.forEach {
                    if (it.error != null) {
                        it.requestFocus()
                        return@setOnClickListener
                    }
                }
                viewModel.register(
                    username = etUsername.text.toString(),
                    name = etName.text.toString(),
                    password = etPassword.text.toString(),
                    email = etEmail.text.toString(),
                )
            }
            btnRegisterGoogle.setOnClickListener {
                val googleIdOption: GetGoogleIdOption =
                    GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(false)
                        .setServerClientId(BuildConfig.SERVER_CLIENT_ID).build()
                val request: GetCredentialRequest =
                    GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
                val credentialManager = CredentialManager.create(requireContext())
                btnRegisterGoogle.invisible()
                loadingOauth.visible()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val result = credentialManager.getCredential(requireContext(), request)
                        handleSignUp(result)
                    } catch (e: GetCredentialException) {
                        withContext(Dispatchers.Main) {
                            loadingOauth.invisible()
                            btnRegisterGoogle.visible()
                            Timber.e(e.message)
                        }
                    }
                }
            }

        }
    }

    override fun setupObservers() {
        with(binding) {
            viewModel.registerResponse.observe(viewLifecycleOwner) {
                when (it) {
                    State.Empty -> {}
                    is State.Error -> {
                        loadingRegister.invisible()
                        btnRegister.visible()
                    }

                    State.Loading -> {
                        btnRegister.invisible()
                        loadingRegister.visible()
                    }

                    is State.Success -> {
                        requireActivity().toastLong("Successfully created an account")
                        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
                    }
                }
            }
            viewModel.oAuthResponse.observe(viewLifecycleOwner) {
                when (it) {
                    State.Empty, is State.Error -> {
                        loadingOauth.invisible()
                        btnRegisterGoogle.visible()
                    }

                    State.Loading -> {
                        btnRegisterGoogle.invisible()
                        loadingOauth.visible()
                    }

                    is State.Success -> {
                        loadingOauth.invisible()
                        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment())
                    }
                }
            }
        }
    }

    private fun handleSignUp(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        Timber.d(googleIdTokenCredential.idToken)
                        viewModel.oAuthGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Timber.e("Received an invalid google id token response", e)
                    }
                } else {
                    Timber.e("Unexpected type of credential")
                }
            }

            else -> {
                Timber.e("Unexpected type of credential")
            }
        }
    }
}