package id.ac.istts.ecotong.ui.profile

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.BuildConfig
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.data.repository.State
import id.ac.istts.ecotong.databinding.FragmentProfileBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import id.ac.istts.ecotong.util.toastLong
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels()
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri == null || uri == Uri.EMPTY) {
            return@registerForActivityResult
        } else {
            val file = uriToFile(uri, requireContext())
            viewModel.updateProfilePicture(file)
        }
    }

    override fun initData() {
        viewModel.getProfile()
    }

    override fun setupUI() {
    }

    override fun setupListeners() {
        with(binding) {
            goToSettings.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSettingsFragment())
            }

            ivChangePp.setOnClickListener {
                launcherGallery.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
            postedPosts.setOnClickListener {
                val slideIn = ObjectAnimator.ofFloat(savedPosts, "translationX", -100f, 0f)
                slideIn.duration = 300
                slideIn.start()

                savedPosts.setBackgroundResource(android.R.color.transparent)
                postedPosts.setBackgroundResource(R.drawable.bottom_border)

                postedPosts.setTypeface(null, Typeface.BOLD)
                savedPosts.setTypeface(null, Typeface.NORMAL)
            }
            savedPosts.setOnClickListener {
                val slideIn = ObjectAnimator.ofFloat(postedPosts, "translationX", 100f, 0f)
                slideIn.duration = 300
                slideIn.start()

                postedPosts.setBackgroundResource(android.R.color.transparent)
                savedPosts.setBackgroundResource(R.drawable.bottom_border)

                savedPosts.setTypeface(null, Typeface.BOLD)
                postedPosts.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    override fun setupObservers() {
        viewModel.profile.observe(viewLifecycleOwner) {
            when (it) {
                State.Empty -> {}
                is State.Error -> {
                    Timber.e(it.error)
                }

                State.Loading -> {}
                is State.Success -> {
                    with(binding) {
                        tvEmail.text = it.data.email
                        tvUsername.text = it.data.username
                        Glide.with(this.root.context)
                            .load(BuildConfig.API_BASE_URL + "profilepictures/fotoprofile-${it.data.username}.jpg")
                            .placeholder(
                                R.drawable.account_circle
                            ).error(R.drawable.account_circle)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).into(ivPP)
                    }
                }
            }
        }
        viewModel.profilePictureResponse.observe(viewLifecycleOwner) {
            when (it) {
                is State.Success -> {
                    viewModel.getProfile(true)
                }

                is State.Loading -> {}

                else -> {
                    requireActivity().toastLong("Failed to upload picture")
                }
            }
        }
    }

    private fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        return File.createTempFile("temp_pp_gallery", ".jpg", filesDir)
    }

    private fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(
            buffer, 0, length
        )
        outputStream.close()
        inputStream.close()
        return myFile
    }

}