package com.walker.dripstone.home.headline

import androidx.fragment.app.Fragment
import com.walker.common.fragment.EmptyFragment
import com.walker.common.router.IOptimizeRouter
import com.walker.common.router.IStudyRouter
import com.walker.common.router.IUiRouter
import com.walker.core.base.mvvm.model.MvvmBaseModel
import com.walker.core.router.RouterLoader
import com.walker.core.util.GsonUtils
import com.walker.core.util.Utils
import com.walker.dripstone.home.Channel
import com.walker.dripstone.home.HomeChannels
import com.walker.dripstone.home.MockHomeChannels
import kotlinx.coroutines.*

class HomeChannelModel(private val viewModelScope: CoroutineScope) :
    MvvmBaseModel<HomeChannels, ArrayList<Channel>>(
        HomeChannels::class.java
    ) {

    companion object {
        const val DEFAULT_CHANNEL_DATA = "{\n" +
                "    \"channelList\": [\n" +
                "        {\n" +
                "            \"channelId\": \"101\",\n" +
                "            \"channelName\": \"UI\",\n" +
                "            \"uri\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"channelId\": \"102\",\n" +
                "            \"channelName\": \"优化\",\n" +
                "            \"uri\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"channelId\": \"103\",\n" +
                "            \"channelName\": \"算法\",\n" +
                "            \"uri\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"channelId\": \"104\",\n" +
                "            \"channelName\": \"数据\",\n" +
                "            \"uri\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"channelId\": \"105\",\n" +
                "            \"channelName\": \"工具\",\n" +
                "            \"uri\": \"\"\n" +
                "        }\n" +
                "    ]\n" +
                "}"

        fun createFragment(key: String): Fragment {
            var fragment: Fragment? = null
            when (key) {
                "100" -> {
                    val studyProvider = RouterLoader.load(IStudyRouter::class.java)
                    fragment = studyProvider?.getSummaryFragment()
                }
                "101" -> {
                    val uiRouter = RouterLoader.load(IUiRouter::class.java)
                    fragment = uiRouter?.getSummaryFragment()
                }
                "102" -> {
                    val optimizeProvider = RouterLoader.load(IOptimizeRouter::class.java)
                    fragment = optimizeProvider?.getSummaryFragment()
                }
            }
            fragment ?: let { fragment = EmptyFragment.instance() }
            return fragment!!
        }
    }

    override fun onSuccess(data: HomeChannels?, isFromCache: Boolean) {
        loadSuccess(data, data?.channelList, isFromCache)
    }

    override fun onFailure(e: Throwable?) {
        loadFail(e?.message)
    }

    override fun refresh() {
    }

    override fun load() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                val data = mockData()
                takeIf { data != null }?.also {
                    onSuccess(data, false)
                } ?: onFailure(Throwable("数据加载失败"))
            }
        }
    }

    suspend fun mockData(): HomeChannels? {
        var homeChannels: HomeChannels? = null
        withContext(Dispatchers.IO) {
            var jsonData = MockHomeChannels.getChannels()
            if (Utils.isEmpty(jsonData)) {
                jsonData = DEFAULT_CHANNEL_DATA
            }
            homeChannels = GsonUtils.fromLocalJson(jsonData, HomeChannels::class.java)
        }
        return homeChannels
    }
}