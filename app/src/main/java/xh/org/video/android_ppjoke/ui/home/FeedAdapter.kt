package xh.org.video.android_ppjoke.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.annotations.NotNull
import xh.org.video.android_ppjoke.databinding.LayoutFeedTypeImageBinding
import xh.org.video.android_ppjoke.databinding.LayoutFeedTypeVideoBinding
import xh.org.video.android_ppjoke.model.Feed

class FeedAdapter : PagedListAdapter<Feed, FeedAdapter.ViewHolder> {
    private val inflater: LayoutInflater
    private val mContext: Context
    protected val mCategory: String

    constructor(context: Context, category: String) : super(object : DiffUtil.ItemCallback<Feed>() {
        override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return false
        }

    }) {
        inflater = LayoutInflater.from(context)
        mContext = context
        mCategory = category
    }

    override fun getItemViewType(position: Int): Int {
        val feed = getItem(position)
        return feed!!.itemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            if (viewType == Feed.TYPE_IMAGE) {
                LayoutFeedTypeImageBinding.inflate(inflater, parent, false)
            } else {
                LayoutFeedTypeVideoBinding.inflate(inflater, parent, false)
            }
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bindData(it) }
    }

    inner class ViewHolder(@NotNull item: View, val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(item) {
        fun bindData(item: Feed) {
            if (binding is LayoutFeedTypeImageBinding) {
                val imageBinding = binding as LayoutFeedTypeImageBinding
                imageBinding.feed = item
                imageBinding.feedImage.bind(item.width, item.height, 16, url = item.cover)
            } else {
                val videoBinding = binding as LayoutFeedTypeVideoBinding
                videoBinding.feed = item
                videoBinding.listPlayerView.bindData(
                    mCategory,
                    item.width,
                    item.height,
                    item.cover,
                    item.url
                )
            }
        }
    }
}