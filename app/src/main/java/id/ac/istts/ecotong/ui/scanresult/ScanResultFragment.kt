package id.ac.istts.ecotong.ui.scanresult

import android.os.Build
import android.text.Html
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.data.repository.State
import id.ac.istts.ecotong.databinding.FragmentScanResultBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import org.markdown4j.Markdown4jProcessor
import timber.log.Timber

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ScanResultFragment :
    BaseFragment<FragmentScanResultBinding>(FragmentScanResultBinding::inflate) {
    private val args: ScanResultFragmentArgs by navArgs()
    private val viewModel: ScanResultViewModel by viewModels()
    override fun initData() {
        viewModel.getAiResponse(args.madeFrom)
    }

    override fun setupUI() {

    }

    override fun setupListeners() {

    }

    override fun setupObservers() {
        viewModel.aiResponse.observe(viewLifecycleOwner) {
            when (it) {
                State.Empty -> {}
                is State.Error -> {
                    Timber.e(it.error)
                }
                State.Loading -> {}
                is State.Success -> {
                    with(binding) {
                        tvResult.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(
                                Markdown4jProcessor().process(it.data),
                                Html.FROM_HTML_MODE_COMPACT
                            )
                        } else {
                            Html.fromHtml(Markdown4jProcessor().process(it.data))
                        }
                    }
                }
            }
        }
    }
}