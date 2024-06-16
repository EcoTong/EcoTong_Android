package id.ac.istts.ecotong.ui.activequest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import id.ac.istts.ecotong.R
abstract class ActiveQuestFragment<FragmentActiveQuestBinding : ViewBinding>(
    private val inflate: (layoutInflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean) -> FragmentActiveQuestBinding
) : Fragment() {
    private var _binding: FragmentActiveQuestBinding? = null

    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun setupUI()
    abstract fun setupListeners()
    abstract fun setupObservers()
}