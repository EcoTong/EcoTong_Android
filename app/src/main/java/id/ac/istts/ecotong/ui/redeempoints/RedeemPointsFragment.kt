package id.ac.istts.ecotong.ui.redeempoints

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import id.ac.istts.ecotong.databinding.FragmentRedeemPointsBinding
import id.ac.istts.ecotong.ui.base.BaseFragment

class RedeemPointsFragment: BaseFragment<FragmentRedeemPointsBinding>(FragmentRedeemPointsBinding::inflate) {
    override fun setupUI() {
    }
    override fun setupListeners() {
        with(binding) {
            toolbarPost.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
    override fun setupObservers() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
        setupObservers()
    }
}