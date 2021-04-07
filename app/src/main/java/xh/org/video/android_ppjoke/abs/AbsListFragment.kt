package xh.org.video.android_ppjoke.abs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
 import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import xh.org.video.android_ppjoke.R
import xh.org.video.android_ppjoke.databinding.LayoutRefreshViewBinding
import xh.org.video.common.widget.EmptyView
import java.lang.reflect.ParameterizedType

abstract class AbsListFragment<K,T, M : AbsViewModel<K, T>> : Fragment(), OnRefreshListener,
    OnLoadMoreListener {
    protected lateinit var binding: LayoutRefreshViewBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshView: SmartRefreshLayout
    private lateinit var emptyView: EmptyView
    protected lateinit var mAdapter: PagedListAdapter<T, RecyclerView.ViewHolder>
    protected lateinit var viewModel: M
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutRefreshViewBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        refreshView = binding.refreshLayout
        refreshView.setEnableRefresh(true)
        refreshView.setEnableLoadMore(true)
        refreshView.setOnRefreshListener(this)
        refreshView.setOnLoadMoreListener(this)
        mAdapter = getAdapter()
        emptyView = binding.emptyView
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = null
        val direction = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        ContextCompat.getDrawable(this.context!!, R.drawable.list_divider)
            ?.let { direction.setDrawable(it) }
        recyclerView.addItemDecoration(direction)
        genericViewModel()
        return binding.root

    }

    private fun genericViewModel() {
        //获取直接的父类的类型
        val type = javaClass.genericSuperclass as ParameterizedType
        val arguments = type.actualTypeArguments
        if (arguments.size > 2) {
            val argument = arguments[2]
            val modelClaz = (argument as Class<*>).asSubclass(AbsViewModel::class.java)
            viewModel = ViewModelProviders.of(this).get(modelClaz) as M
            viewModel.pageData.observe(viewLifecycleOwner, Observer {
                submitList(it)
            })
            viewModel.boundaryPageData.observe(viewLifecycleOwner, Observer {
                finishRefresh(it)
            })

        }
    }

    fun submitList(result: PagedList<T>) {
        mAdapter.submitList(result)
        finishRefresh(result.size > 0)
    }

    private fun finishRefresh(hasData: Boolean) {
        var hasData = hasData
        val currentList = mAdapter.currentList
        hasData = hasData || currentList != null && currentList.size > 0
        val state: RefreshState = refreshView.getState()
        if (state.isFooter && state.isOpening) {
            refreshView.finishLoadMore()
        } else if (state.isHeader && state.isOpening) {
            refreshView.finishRefresh()
        }
        if (hasData) {
            emptyView.visibility = View.GONE
        } else {
            emptyView.visibility = View.VISIBLE
        }
    }


    abstract fun getAdapter(): PagedListAdapter<T, RecyclerView.ViewHolder>

}