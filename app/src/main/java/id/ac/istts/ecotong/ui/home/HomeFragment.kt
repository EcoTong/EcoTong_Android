package id.ac.istts.ecotong.ui.home

import androidx.navigation.fragment.findNavController
import id.ac.istts.ecotong.databinding.FragmentHomeBinding
import id.ac.istts.ecotong.ui.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    override fun setupUI() {
        with(binding) {
            
        }
    }

    override fun setupListeners() {
        with(binding){
            fabPost.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPostFragment())
            }
            tvShowMore.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionGlobalActiveQuestFragment())
            }
        }
    }

    override fun setupObservers() {
    }
}