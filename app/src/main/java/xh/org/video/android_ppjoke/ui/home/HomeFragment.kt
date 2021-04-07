package xh.org.video.android_ppjoke.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import xh.org.video.android_ppjoke.model.Feed
import xh.org.video.android_ppjoke.abs.AbsListFragment
import xh.org.video.android_ppjoke.ui.MutableDataSource
import xh.org.video.libannotation.FragmentDestination

@FragmentDestination(pageUrl = "main/tabs/home", isStart = true)
class HomeFragment : AbsListFragment<Int, Feed, HomeViewModel>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.cacheLiveData.observe(HomeFragment@ this,
            Observer<PagedList<Feed>> {
                submitList(it)
            })
    }


    override fun onRefresh(refreshLayout: RefreshLayout) {
        viewModel.getDataSource()!!.invalidate()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        val feed = mAdapter.currentList!![mAdapter.itemCount - 1]
        viewModel.loadAfter(feed!!.id, object : ItemKeyedDataSource.LoadCallback<Feed>() {
            override fun onResult(data: MutableList<Feed>) {
                val config = mAdapter.currentList!!.config
                if (data != null && data.size > 0) {
                    val dataSource = MutableDataSource<Int, Feed>()
                    dataSource.data.addAll(data)
                    val pageList = dataSource.buildNewPageList(config)
                    submitList(pageList)
                }

            }
        })

    }

    override fun getAdapter(): PagedListAdapter<Feed, RecyclerView.ViewHolder> {
        val feedType =
            if (arguments == null) "all" else requireArguments().getString("feedType")!!
        return FeedAdapter(
            requireContext(),
            feedType
        ) as PagedListAdapter<Feed, RecyclerView.ViewHolder>
    }
}