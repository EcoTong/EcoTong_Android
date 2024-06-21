package id.ac.istts.ecotong.ui.profile

import android.animation.ObjectAnimator
import android.graphics.Typeface
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.data.repository.State
import id.ac.istts.ecotong.databinding.FragmentProfileBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels()
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

            goToRedeem.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToRedeemPointsFragment())
            }

            goToCompletedQuest.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToCompletedQuestFragment())
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
                    }
                }
            }
        }
    }

}