package id.ac.istts.ecotong.ui.detailquest

import android.os.Bundle
import android.view.View
import id.ac.istts.ecotong.databinding.FragmentDetailQuestBinding
import id.ac.istts.ecotong.ui.base.BaseFragment

class DetailQuestFragment: BaseFragment<FragmentDetailQuestBinding>(FragmentDetailQuestBinding::inflate) {
    override fun setupUI() {
    }
    override fun setupListeners() {
        with(binding) {

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