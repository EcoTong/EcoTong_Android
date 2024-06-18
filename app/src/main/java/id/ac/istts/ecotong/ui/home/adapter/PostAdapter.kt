package id.ac.istts.ecotong.ui.home.adapter


import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import id.ac.istts.ecotong.BuildConfig
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.data.remote.response.Post
import id.ac.istts.ecotong.databinding.ItemPostBinding
import id.ac.istts.ecotong.util.createSpannableString
import id.ac.istts.ecotong.util.getRelativeTime

class PostAdapter(
    private val showComments: (String) -> Unit,
    private val likePost: (String) -> Unit,
    private val unlikePost: (String) -> Unit,
    private val bookmarkPost: (String) -> Unit,
    private val unbookmarkPost: (String) -> Unit
) : ListAdapter<Post, PostAdapter.PostViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvDescription.text = createSpannableString(item.title, item.description)
            tvTimestamps.text = getRelativeTime(item.createdAt)
            tvName.text = item.username
            tvLikeCount.text = item.likes.toString()
            tvCommentsCount.text = item.comments.toString()
            ivComments.setOnClickListener {
                showComments.invoke(item.id)
            }
            if (item.bookmarked != null && item.bookmarked) {
                ivBookmark.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context, R.drawable.bookmark_saved_icon
                    )
                )
            } else {
                ivBookmark.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context, R.drawable.bookmark_icon
                    )
                )
            }
            if (item.liked != null && item.liked) {
                ivLike.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context, R.drawable.red_heart
                    )
                )
                ivLike.contentDescription = "liked"
            } else {
                ivLike.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context, R.drawable.white_heart
                    )
                )
                ivLike.contentDescription = "notliked"
            }
            ivLike.setOnClickListener {
                if (ivLike.contentDescription == "liked") {
                    unlikePost.invoke(item.id)
                    ivLike.setImageDrawable(
                        ContextCompat.getDrawable(
                            root.context, R.drawable.white_heart
                        )
                    )
                    ivLike.contentDescription = "notliked"
                    tvLikeCount.text = (tvLikeCount.text.toString().toInt() - 1).toString()
                } else {
                    likePost.invoke(item.id)
                    ivLike.setImageDrawable(
                        ContextCompat.getDrawable(
                            root.context, R.drawable.red_heart
                        )
                    )
                    ivLike.contentDescription = "liked"
                    tvLikeCount.text = (tvLikeCount.text.toString().toInt() + 1).toString()
                }
            }
            ivBookmark.setOnClickListener {
                if (ivBookmark.contentDescription == "bookmarked") {
                    unbookmarkPost.invoke(item.id)
                    ivBookmark.setImageDrawable(
                        ContextCompat.getDrawable(
                            root.context, R.drawable.bookmark_icon
                        )
                    )
                    ivBookmark.contentDescription = "notbookmarked"
                } else {
                    bookmarkPost.invoke(item.id)
                    ivBookmark.setImageDrawable(
                        ContextCompat.getDrawable(
                            root.context, R.drawable.bookmark_saved_icon
                        )
                    )
                    ivBookmark.contentDescription = "bookmarked"
                }
            }
            val gestureDetector =
                GestureDetector(root.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        if (ivLike.contentDescription == "liked") {
                            unlikePost.invoke(item.id)
                            ivLike.setImageDrawable(
                                ContextCompat.getDrawable(
                                    root.context, R.drawable.white_heart
                                )
                            )
                            ivLike.contentDescription = "notliked"
                            tvLikeCount.text = (tvLikeCount.text.toString().toInt() - 1).toString()
                        } else {
                            likePost.invoke(item.id)
                            ivLike.setImageDrawable(
                                ContextCompat.getDrawable(
                                    root.context, R.drawable.red_heart
                                )
                            )
                            ivLike.contentDescription = "liked"
                            tvLikeCount.text = (tvLikeCount.text.toString().toInt() + 1).toString()
                        }
                        return true
                    }
                })
            ivPost.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                true
            }
            val circularProgressDrawable = CircularProgressDrawable(root.context)
            circularProgressDrawable.strokeWidth = 16f
            circularProgressDrawable.centerRadius = 200f
            circularProgressDrawable.start()
            Glide.with(this.root.context)
                .load(BuildConfig.API_BASE_URL + "postpictures/" + item.picture).placeholder(
                    circularProgressDrawable
                ).error(R.drawable.ic_broken_image).into(ivPost)
            Glide.with(this.root.context)
                .load(BuildConfig.API_BASE_URL + "profilepictures/fotoprofile${item.username}.jpg")
                .placeholder(
                    circularProgressDrawable
                ).error(R.drawable.account_circle).into(ivUser)
        }
    }

    inner class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

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