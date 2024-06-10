package id.ac.istts.ecotong.ui.auth.login

import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import id.ac.istts.ecotong.BuildConfig
import id.ac.istts.ecotong.databinding.FragmentLoginBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    override fun setupUI() {
        with(binding) {

        }
    }

    override fun setupListeners() {
        with(binding) {
            tvToSignUp.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }
            btnLogin.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
            }
            btnLoginGoogle.setOnClickListener {
                val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.SERVER_CLIENT_ID)
                    .build()

                val request: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
                val credentialManager = CredentialManager.create(requireContext())
                coroutineScope.launch {
                    try {
                        val result = credentialManager.getCredential(requireContext(), request)
                        handleSignIn(result)
                    } catch (e: GetCredentialException) {
                        Timber.e("MainActivity", "GetCredentialException", e)
                    }
                }

            }
        }
    }

    override fun setupObservers() {
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        Timber.d(googleIdTokenCredential.idToken)
                        // Send googleIdTokenCredential to your server for validation and authentication
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