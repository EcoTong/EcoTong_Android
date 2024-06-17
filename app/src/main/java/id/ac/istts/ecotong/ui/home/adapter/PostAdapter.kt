package id.ac.istts.ecotong.ui.home.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.istts.ecotong.BuildConfig
import id.ac.istts.ecotong.data.remote.response.Post
import id.ac.istts.ecotong.databinding.ItemPostBinding
import id.ac.istts.ecotong.util.getRelativeTime

class PostAdapter(private val showComments: () -> Unit) :
    ListAdapter<Post, PostAdapter.PostViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvDescription.text = item.description
            tvTimestamps.text = getRelativeTime(item.createdAt)
            tvName.text = item.username
            ivComments.setOnClickListener {
            showComments.invoke()
            }
            Glide.with(this.root.context)
                .load(BuildConfig.API_BASE_URL + "postpictures/" + item.picture).into(ivPost)
        }
    }

    inner class PostViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }
}