package id.ac.istts.ecotong.ui.auth.login

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
import id.ac.istts.ecotong.databinding.FragmentLoginBinding
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
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel: LoginViewModel by viewModels()
    override fun setupUI() {
        with(binding) {

        }
    }

    override fun setupListeners() {
        with(binding) {
            tvToSignUp.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }
            etEmail.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrEmpty()) {
                    etEmail.error = getString(R.string.please_fill_out_this_field)
                    return@doOnTextChanged
                }
                if (!text.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) {
                    etEmail.error = getString(R.string.invalid_email_format)
                    return@doOnTextChanged
                }
            }
            etPassword.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrEmpty()) {
                    etPassword.error = getString(R.string.please_fill_out_this_field)
                    return@doOnTextChanged
                }
            }
            btnLogin.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                val inputFields = listOf(etEmail, etPassword)
                inputFields.forEach {
                    if (it.text.isEmpty()) {
                        it.error = getString(R.string.please_fill_out_this_field)
                    }
                }
                inputFields.forEach {
                    if (it.error != null) {
                        it.requestFocus()
                        return@setOnClickListener
                    }
                }
                viewModel.login(
                    email = etEmail.text.toString(),
                    password = etPassword.text.toString()
                )
            }
            btnLoginGoogle.setOnClickListener {
                val googleIdOption: GetGoogleIdOption =
                    GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(false)
                        .setServerClientId(BuildConfig.SERVER_CLIENT_ID).build()
                val request: GetCredentialRequest =
                    GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
                val credentialManager = CredentialManager.create(requireContext())
                btnLoginGoogle.invisible()
                loadingOauth.visible()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val result = credentialManager.getCredential(requireContext(), request)
                        handleSignIn(result)
                    } catch (e: GetCredentialException) {
                        withContext(Dispatchers.Main) {
                            loadingOauth.invisible()
                            btnLoginGoogle.visible()
                            Timber.e(e.message)
                        }
                    }
                }

            }
        }
    }

    override fun setupObservers() {
        with(binding) {
            viewModel.oAuthResponse.observe(viewLifecycleOwner) {
                when (it) {
                    State.Empty -> {}
                    is State.Error -> {
                        loadingOauth.invisible()
                        btnLoginGoogle.visible()
                    }

                    State.Loading -> {
                        btnLoginGoogle.invisible()
                        loadingOauth.visible()
                    }

                    is State.Success -> {
                        loadingOauth.invisible()
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    }
                }
            }
            viewModel.loginResponse.observe(viewLifecycleOwner) {
                when (it) {
                    State.Empty -> {
                        loadingLogin.invisible()
                        btnLogin.visible()
                    }

                    is State.Error -> {
                        loadingLogin.invisible()
                        btnLogin.visible()
                        requireActivity().toastLong(it.error)
                        when (it.error) {
                            "Account Not Found", "Wrong Password" -> {
                                requireActivity().toastLong(getString(R.string.invalid_email_or_password))
                            }

                            else -> {
                                requireActivity().toastLong(getString(R.string.an_unexpected_error_has_occurred))
                            }
                        }
                    }

                    State.Loading -> {
                        btnLogin.invisible()
                        loadingLogin.visible()
                    }

                    is State.Success -> {
                        loadingLogin.invisible()
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    }
                }
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
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