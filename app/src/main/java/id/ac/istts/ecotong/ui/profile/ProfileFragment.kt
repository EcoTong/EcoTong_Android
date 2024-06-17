package id.ac.istts.ecotong.ui.profile

import android.animation.ObjectAnimator
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.navigation.fragment.findNavController
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.databinding.FragmentProfileBinding
import id.ac.istts.ecotong.ui.base.BaseFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    override fun setupUI() {
    }

    override fun setupListeners() {
        with(binding) {
            goToSettings.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionGlobalSettingsFragment())
            }

            goToRedeem.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionGlobalRedeemPointsFragment2())
            }

            goToCompletedQuest.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionGlobalCompletedQuestFragment())
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
    }

}