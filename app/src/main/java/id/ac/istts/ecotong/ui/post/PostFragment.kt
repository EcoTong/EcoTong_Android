package id.ac.istts.ecotong.ui.post

import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.databinding.FragmentPostBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import id.ac.istts.ecotong.util.invisible
import id.ac.istts.ecotong.util.visible

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
                                requireContext(),
                                R.drawable.intro_5
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
        }
    }

}