package id.ac.istts.ecotong.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.ecotong.data.remote.response.Comments
import id.ac.istts.ecotong.databinding.ItemCommentBinding
import id.ac.istts.ecotong.util.getRelativeTime

class CommentAdapter :
    ListAdapter<Comments, CommentAdapter.CommentViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvAuthor.text = item.username
            tvContent.text = item.content
            tvCommentTimestamps.text = item.createdAt
            tvCommentTimestamps.text = getRelativeTime(item.createdAt)
        }
    }

    inner class CommentViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Comments>() {
            override fun areItemsTheSame(oldItem: Comments, newItem: Comments): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Comments, newItem: Comments): Boolean {
                return oldItem == newItem
            }
        }
    }
}