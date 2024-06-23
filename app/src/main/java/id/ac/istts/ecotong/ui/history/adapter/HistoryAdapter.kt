package id.ac.istts.ecotong.ui.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.ecotong.data.remote.response.Scan
import id.ac.istts.ecotong.databinding.ItemHistoryBinding
import id.ac.istts.ecotong.util.getRelativeTime


class HistoryAdapter(private val onRootClick: (String, String) -> Unit) :
    ListAdapter<Scan, HistoryAdapter.HistoryViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvMaterial.text = item.madeFrom.uppercase()
            tvTimestamps.text = getRelativeTime(item.createdAt)
            root.setOnClickListener {
                onRootClick.invoke(item.madeFrom, item.instruction)
            }
        }
    }

    inner class HistoryViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Scan>() {
            override fun areItemsTheSame(oldItem: Scan, newItem: Scan): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Scan, newItem: Scan): Boolean {
                return oldItem == newItem
            }
        }
    }
}