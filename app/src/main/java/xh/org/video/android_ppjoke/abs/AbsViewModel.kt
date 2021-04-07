package xh.org.video.android_ppjoke.abs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

/****
 * 一个抽象的ViewModle对象 ,存放网络请求的数据
 */
abstract class AbsViewModel<K, T> : ViewModel() {
    //保存pageList 数据加载状态的对象
    val boundaryPageData = MutableLiveData<Boolean>()

    //pageList加载的数据
    private var dataSource: DataSource<K, T>? = null

    //pageList加载数据的配置
    protected var config: PagedList.Config = PagedList.Config.Builder()
        .setPageSize(10)
        .setInitialLoadSizeHint(12)
        .build()

    //创建DataSource的工厂对象的创建
    private val factory = object : DataSource.Factory<K, T>() {
        override fun create(): DataSource<K, T> {
            if (dataSource == null || dataSource!!.isInvalid) {
                dataSource = createDataSource()
            }
            return dataSource as DataSource<K, T>
        }

    }

    fun getDataSource(): DataSource<K, T>? {
        return dataSource
    }
    //pageList加载数据的回调对象
    private val callback = object : PagedList.BoundaryCallback<T>() {
        override fun onZeroItemsLoaded() {
            boundaryPageData.postValue(false)
        }

        override fun onItemAtFrontLoaded(itemAtFront: T) {
            boundaryPageData.postValue(true)
        }
    }

    //存放pageList数据的LiveData
    var pageData: LiveData<PagedList<T>> = LivePagedListBuilder(factory, config)
        .setBoundaryCallback(callback)
        .build()
    /**
     * 抽象的创建DataSource的方法
     */
    abstract fun createDataSource(): DataSource<K, T>
}