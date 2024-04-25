package id.ac.istts.ecotong.ui.example

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.databinding.FragmentBlankBinding
import id.ac.istts.ecotong.ui.base.BaseFragment

@AndroidEntryPoint
class BlankFragment : BaseFragment<FragmentBlankBinding>(FragmentBlankBinding::inflate) {
    private val viewModel: BlankViewModel by viewModels()
    override fun setupUI() {
    }

    override fun setupListeners() {
    }

    override fun setupObservers() {
    }
}