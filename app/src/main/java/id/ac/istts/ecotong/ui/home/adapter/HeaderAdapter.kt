package id.ac.istts.ecotong.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.databinding.ItemHeaderBinding

class HeaderAdapter : RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {

    private var username: String? = null
    private var points: String? = null

    inner class ViewHolder(val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(username: String?, points: String?) {
            binding.tvUser.text = username?.let {
                binding.root.context.getString(R.string.hi_user, it)
            } ?: ""
            binding.points.text = points.toString() ?: ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(username, points)
    }

    override fun getItemCount(): Int {
        return 1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(username: String?, points: String?) {
        this.username = username
        this.points = points
        notifyDataSetChanged()
    }
}
