package com.walker.collect.news.headline

import com.walker.collect.MockNewsData
import com.walker.core.base.mvvm.model.MvvmBaseModel
import com.walker.core.util.GsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class NewsChannelModel : MvvmBaseModel<NewsChannels, ArrayList<Channel>>(
    NewsChannels::class.java
) {

    override fun onSuccess(data: NewsChannels?, isFromCache: Boolean) {
        loadSuccess(data, data?.channelList, isFromCache)
    }

    override fun onFailure(e: Throwable?) {
        loadFail(e?.message)
    }

    override fun refresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun load() {
        runBlocking {
            val data = withContext(Dispatchers.Default) { mockData() }
            takeIf { data != null }?.also {
                onSuccess(data, false)
            } ?: onFailure(Throwable("数据加载失败"))
        }
    }

    suspend fun mockData(): NewsChannels? {
        var homeChannels: NewsChannels? = null
        withContext(Dispatchers.IO) {
            var jsonData = MockNewsData.getChannels()
            homeChannels = GsonUtils.fromLocalJson(jsonData, NewsChannels::class.java)
        }
        return homeChannels
    }
}