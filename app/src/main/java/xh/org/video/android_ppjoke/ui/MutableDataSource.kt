package xh.org.video.android_ppjoke.ui

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList

/****
 * PageKeyedDataSource 这种数据原 适用于通过第N次的网路请求的返回结果加载第N+1次的场景
 */
class MutableDataSource<Key, Value> : PageKeyedDataSource<Key, Value>() {
     val data = ArrayList<Value>()


    @SuppressLint("RestrictedApi")
    fun buildNewPageList(config: PagedList.Config): PagedList<Value> {
        return PagedList.Builder(this, config)
            .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
            .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
            .build()
    }

    override fun loadInitial(
        params: LoadInitialParams<Key>,
        callback: LoadInitialCallback<Key, Value>
    ) {
        callback.onResult(data, null, null)
    }

    override fun loadAfter(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
        callback.onResult(emptyList(), null)
    }

    override fun loadBefore(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
        callback.onResult(emptyList(), null)
    }

}