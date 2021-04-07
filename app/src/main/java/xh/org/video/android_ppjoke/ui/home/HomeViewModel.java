package xh.org.video.android_ppjoke.ui.home;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;

import com.alibaba.fastjson.TypeReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import xh.org.video.android_ppjoke.abs.AbsViewModel;
import xh.org.video.android_ppjoke.model.Feed;
import xh.org.video.android_ppjoke.ui.MutableDataSource;
import xh.org.video.libnetwork.ApiResponse;
import xh.org.video.libnetwork.ApiService;
import xh.org.video.libnetwork.JsonCallback;
import xh.org.video.libnetwork.Request;

public class HomeViewModel extends AbsViewModel<Integer, Feed> {
    private volatile boolean witchCache = true;
    // 存放缓存的数据的LiveData对象
    private MutableLiveData<PagedList<Feed>> cacheLiveData = new MutableLiveData<>();

    public MutableLiveData<PagedList<Feed>> getCacheLiveData() {
        return cacheLiveData;
    }

    // 为 false  可以加载下一页的数据
    private AtomicBoolean loadAfter = new AtomicBoolean(false);

    /***
     * 创建DataSource的方法 DataSource理解为是一个存储分页的数据的容器
     * 在当前的这个类中相当于是 通过key - value的形式将分页的数据存储在
     * DataSource中了
     */
    @NotNull
    @Override
    public DataSource<Integer, Feed> createDataSource() {
        return new FeedDataSource();
    }

    class FeedDataSource extends ItemKeyedDataSource<Integer, Feed> {

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Feed> callback) {
            Log.d("count","0");
            //加载初始化数据
            loadData(0, callback);
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            Log.d("count","1");
            //加载下一页数据
            loadData(params.key, callback);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            //向前加载数据
            callback.onResult(Collections.emptyList());
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Feed item) {
            return item.id;
        }
    }

    /***
     * 通过网路请求加载数据 将数据通过onResult()方法 回调存入到 DataSource中
     * @param key
     * @param callback
     */
    private void loadData(int key, ItemKeyedDataSource.LoadCallback<Feed> callback) {
        //当loadAfter 为true的时候进行加载的操作，加载结束之后修改 loadAfter为false
        if (key > 0) {
            loadAfter.set(true);
        }
        //构建请求request
        Request request = ApiService.get("/feeds/queryHotFeedsList")
                .addParam("feedType", null)
                .addParam("userId", 0)
                .addParam("feedId", key)
                .addParam("pageCount", 10)
                .responseType(new TypeReference<ArrayList<Feed>>() {
                }.getType());
        //
        if (witchCache) {
            request.cacheStrategy(Request.CACHE_ONLY);
            //从Room数据库中读取缓存数据
            request.execute(new JsonCallback<List<Feed>>() {
                //缓存成功
                @Override
                public void onCacheSuccess(ApiResponse<List<Feed>> response) {
                    //通过加载成功的数据 构建DataSource 通过LiveData 发送数据
                    MutableDataSource dataSource = new MutableDataSource<Integer, Feed>();
                    dataSource.getData().addAll(response.body);
                    PagedList pagedList = dataSource.buildNewPageList(getConfig());
                    cacheLiveData.postValue(pagedList);
                }
            });
        }
        try {
            Request netRequest = witchCache ? request.clone() : request;
            netRequest.cacheStrategy(key == 0 ? Request.NET_CACHE : Request.NET_ONLY);
            ApiResponse<List<Feed>> response = netRequest.execute();
            List<Feed> data = response.body == null ? Collections.emptyList() : response.body;
            callback.onResult(data);
            if (key > 0) {
                getBoundaryPageData().postValue(data.size() > 0);
                loadAfter.set(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("loadData", "loadData: key:" + key);


    }


    @SuppressLint("RestrictedApi")
    public void loadAfter(int id, ItemKeyedDataSource.LoadCallback<Feed> callback) {
        if (loadAfter.get()) {
            callback.onResult(Collections.emptyList());
            return;
        }
        ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                loadData(id, callback);
            }
        });
    }
}
