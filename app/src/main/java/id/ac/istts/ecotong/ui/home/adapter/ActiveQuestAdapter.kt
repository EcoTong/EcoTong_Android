package id.ac.istts.ecotong.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.ecotong.databinding.ItemQuestBinding

class ActiveQuestAdapter() :
    ListAdapter<String, ActiveQuestAdapter.ActiveQuestViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveQuestViewHolder {
        val binding = ItemQuestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActiveQuestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActiveQuestViewHolder, position: Int) {
        val item = getItem(position)
    }

    inner class ActiveQuestViewHolder(val binding: ItemQuestBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}