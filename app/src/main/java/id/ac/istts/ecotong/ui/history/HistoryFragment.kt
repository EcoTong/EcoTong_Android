package id.ac.istts.ecotong.ui.history

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.data.repository.State
import id.ac.istts.ecotong.databinding.FragmentHistoryBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import id.ac.istts.ecotong.ui.history.adapter.HistoryAdapter
import id.ac.istts.ecotong.util.gone
import id.ac.istts.ecotong.util.visible

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {
    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var historyAdapter: HistoryAdapter
    override fun initData() {
        viewModel.getHistory()
    }

    override fun setupUI() {
        historyAdapter = HistoryAdapter(::openDetail)
        with(binding) {
            rvHistory.adapter = historyAdapter
            rvHistory.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun setupListeners() {}
    override fun setupObservers() {
        viewModel.historyResponse.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    State.Loading -> {
                        rvHistory.gone()
                        tvEmpty.gone()
                        loadingHistory.visible()
                    }

                    is State.Success -> {
                        historyAdapter.submitList(it.data)
                        tvEmpty.gone()
                        loadingHistory.gone()
                        rvHistory.visible()
                    }

                    else -> {
                        loadingHistory.gone()
                        rvHistory.gone()
                        tvEmpty.visible()
                    }
                }
            }
        }
    }

    private fun openDetail(material: String, instruction: String) {
        findNavController().navigate(
            HistoryFragmentDirections.actionHistoryFragmentToDetailQuestFragment(
                material,
                instruction
            )
        )
    }
}