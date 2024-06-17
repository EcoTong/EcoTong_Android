package id.ac.istts.ecotong.ui.activequest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.databinding.FragmentActiveQuestBinding
import id.ac.istts.ecotong.ui.base.BaseFragment

class ActiveQuestFragment: BaseFragment <FragmentActiveQuestBinding>(FragmentActiveQuestBinding::inflate) {
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