package id.ac.istts.ecotong.ui.detailHistory

import android.text.Html
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import id.ac.istts.ecotong.databinding.FragmentDetailHistoryBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import org.markdown4j.Markdown4jProcessor

class DetailHistoryFragment :
    BaseFragment<FragmentDetailHistoryBinding>(FragmentDetailHistoryBinding::inflate) {
    private val args: DetailHistoryFragmentArgs by navArgs()
    override fun setupUI() {
        with(binding) {
            tvMaterial.text = args.material.lowercase().replaceFirstChar(Char::titlecase)
            tvResult.text =
                Html.fromHtml(
                    Markdown4jProcessor().process(args.instruction),
                    Html.FROM_HTML_MODE_COMPACT
                )
        }
    }

    override fun setupListeners() {
        with(binding) {
            toolbarPost.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun setupObservers() {

    }
}