package id.ac.istts.ecotong.ui.welcome

import android.view.View
import androidx.core.content.ContextCompat
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.databinding.FragmentWelcomePagerItemBinding
import id.ac.istts.ecotong.ui.base.BaseFragment

class WelcomePagerItemFragment :
    BaseFragment<FragmentWelcomePagerItemBinding>(FragmentWelcomePagerItemBinding::inflate) {
    override fun setupUI() {
        val position = arguments?.getInt(ARG_POSITION)
        with(binding) {
            when (position) {
                0 -> {
                    tvTitle.text = getString(R.string.intro1)
                    ivBanner.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(), R.drawable.intro_1
                        )
                    )
                }

                1 -> {
                    tvTitle.text = getString(R.string.intro2)
                    ivBanner.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(), R.drawable.intro_2
                        )
                    )
                }

                2 -> {
                    tvTitle.text = getString(R.string.intro3)
                    ivBanner.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(), R.drawable.intro_3
                        )
                    )
                }

                3 -> {
                    tvTitle.text = getString(R.string.intro4)
                    ivBanner.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(), R.drawable.intro_4
                        )
                    )
                }

                4 -> {
                    tvTitle.apply {
                        text = getString(R.string.intro5)
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }
                    ivBanner.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(), R.drawable.intro_5
                        )
                    )
                }
            }
        }
    }

    override fun setupListeners() {}

    override fun setupObservers() {}

    companion object {
        const val ARG_POSITION = "position"
    }
}