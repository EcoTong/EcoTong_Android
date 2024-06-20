package id.ac.istts.ecotong.ui.scanresult

import android.text.Html
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.data.repository.State
import id.ac.istts.ecotong.databinding.FragmentScanResultBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import id.ac.istts.ecotong.util.gone
import id.ac.istts.ecotong.util.visible
import org.markdown4j.Markdown4jProcessor
import timber.log.Timber

@AndroidEntryPoint
class ScanResultFragment :
    BaseFragment<FragmentScanResultBinding>(FragmentScanResultBinding::inflate) {
    private val args: ScanResultFragmentArgs by navArgs()
    private val viewModel: ScanResultViewModel by viewModels()
    override fun initData() {
        viewModel.getAiResponse(args.madeFrom)
    }

    override fun setupUI() {
        binding.tvMaterial.text = args.madeFrom.lowercase().replaceFirstChar(Char::titlecase)
    }

    override fun setupListeners() {
        binding.layoutBackScan.setOnClickListener {
            findNavController().navigate(ScanResultFragmentDirections.actionScanResultFragmentToScanFragment())
        }
        binding.layoutBackHome.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun setupObservers() {
        viewModel.aiResponse.observe(viewLifecycleOwner) {
            when (it) {
                State.Empty -> {}
                is State.Error -> {
                    Timber.e(it.error)
                }

                State.Loading -> {
                    binding.loadingResult.visible()
                }
                is State.Success -> {
                    with(binding) {
                        loadingResult.gone()
                        tvResult.text =
                            Html.fromHtml(
                                Markdown4jProcessor().process(it.data),
                                Html.FROM_HTML_MODE_COMPACT
                            )
                    }
                }
            }
        }
    }
}