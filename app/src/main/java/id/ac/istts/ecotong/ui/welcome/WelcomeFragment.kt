package id.ac.istts.ecotong.ui.welcome

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.data.repository.State
import id.ac.istts.ecotong.databinding.FragmentWelcomeBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import id.ac.istts.ecotong.util.invisible
import id.ac.istts.ecotong.util.visible
import timber.log.Timber


private const val NUM_PAGES = 5

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(FragmentWelcomeBinding::inflate) {
    private lateinit var pagerAdapter: ScreenSlidePagerAdapter
    private val viewModel: WelcomeViewModel by viewModels()

    override fun setupUI() {
        with(binding) {
            viewModel.checkToken()
            pagerAdapter = ScreenSlidePagerAdapter(this@WelcomeFragment)
            vpWelcome.apply {
                isUserInputEnabled = false
                this.adapter = pagerAdapter
            }
            dotsIndicator.attachTo(vpWelcome)
            btnStart
        }
    }

    override fun setupListeners() {
        with(binding) {
            requireActivity().onBackPressedDispatcher.addCallback(this@WelcomeFragment,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        Timber.d(vpWelcome.currentItem.toString())
                        if (vpWelcome.currentItem == 0) {
                            if (!findNavController().popBackStack()) {
                                requireActivity().finish()
                            }
                        } else {
                            vpWelcome.currentItem -= 1
                        }
                    }
                }.also {
                    it.isEnabled = (vpWelcome.currentItem == 0)
                })
            ibNext.setOnClickListener {
                vpWelcome.currentItem += 1
            }
            vpWelcome.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    when (position) {
                        4 -> {
                            ibNext.isInvisible = true
                            dotsIndicator.isInvisible = true
                            btnStart.isVisible = true
                        }

                        else -> {
                            ibNext.isVisible = true
                            dotsIndicator.isVisible = true
                            btnStart.isInvisible = true
                        }
                    }
                }
            })
            btnStart.setOnClickListener {
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToToLoginFragment())
            }
        }
    }

    override fun setupObservers() {
        with(binding) {
            viewModel.checkTokenResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is State.Error, State.Empty -> {
                        progressBar.invisible()
                        layoutBoarding.visible()
                    }

                    State.Loading -> {
                        layoutBoarding.invisible()
                        progressBar.visible()
                    }

                    is State.Success -> {
                        progressBar.invisible()
                        findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment())
                    }
                }
            }
        }
    }

    private inner class ScreenSlidePagerAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = NUM_PAGES
        override fun createFragment(position: Int): Fragment {
            val fragment = WelcomePagerItemFragment()
            fragment.arguments = Bundle().apply {
                putInt(WelcomePagerItemFragment.ARG_POSITION, position)
            }
            return fragment
        }
    }
}