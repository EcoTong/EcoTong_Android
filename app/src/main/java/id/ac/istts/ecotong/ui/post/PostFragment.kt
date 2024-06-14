package id.ac.istts.ecotong.ui.post

import android.content.Context
import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.data.repository.State
import id.ac.istts.ecotong.databinding.FragmentPostBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import id.ac.istts.ecotong.util.invisible
import id.ac.istts.ecotong.util.toastLong
import id.ac.istts.ecotong.util.visible
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>(FragmentPostBinding::inflate) {
    private val viewModel: PostViewModel by viewModels()
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri == null || uri == Uri.EMPTY) {
            return@registerForActivityResult
        } else {
            viewModel.setImageUri(uri)
        }
    }

    override fun setupUI() {
    }

    override fun setupListeners() {
        with(binding) {
            toolbarPost.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            btnAddPicture.setOnClickListener {
                launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            ivPreview.setOnClickListener {
                launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            toolbarPost.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_post -> {
                        val tempUri = viewModel.imageUri.value
                        if (tempUri == null || tempUri == Uri.EMPTY) {
                            requireActivity().toastLong(getString(R.string.please_choose_an_image))
                            return@setOnMenuItemClickListener true
                        }
                        val inputFields = arrayOf(etTitle, etDescription)
                        inputFields.forEach { et ->
                            if (et.text.toString().isEmpty()) {
                                et.apply {
                                    error = getString(R.string.please_fill_out_this_field)
                                    requestFocus()
                                    return@setOnMenuItemClickListener true
                                }
                            }
                        }
                        val imageFile = uriToFile(imageUri = tempUri, requireContext())
                        viewModel.addPost(
                            imageFile,
                            etTitle.text.toString(),
                            etDescription.text.toString()
                        )
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        }
    }

    override fun setupObservers() {
        with(binding) {
            viewModel.imageUri.observe(viewLifecycleOwner) {
                when (it) {
                    null, Uri.EMPTY -> {
                        ivPreview.invisible()
                        tvTapToChange.invisible()
                        btnAddPicture.visible()
                        ivPreview.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(), R.drawable.intro_5
                            )
                        )
                    }

                    else -> {
                        ivPreview.setImageURI(null)
                        ivPreview.setImageURI(it)
                        btnAddPicture.invisible()
                        ivPreview.visible()
                        tvTapToChange.visible()
                    }
                }
            }
            viewModel.postResponse.observe(viewLifecycleOwner) {
                when (it) {
                    State.Empty -> {
                        loadingOverlay.invisible()
                    }

                    is State.Error -> {
                        requireActivity().toastLong(getString(R.string.an_unexpected_error_has_occurred))
                        loadingOverlay.invisible()
                    }

                    State.Loading -> {
                        loadingOverlay.visible()
                    }

                    is State.Success -> {
                        loadingOverlay.invisible()
                        requireActivity().toastLong(getString(R.string.upload_post_successful))
                        findNavController().navigate(PostFragmentDirections.actionPostFragmentToHomeFragment())
                    }
                }
            }
        }
    }

    private fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        return File.createTempFile("temp_post_upload_gallery", ".jpg", filesDir)
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