package com.walker.dripstone.home.headline

import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.walker.common.arouter.RouteServiceManager
import com.walker.common.arouter.ui.IUiProvider
import com.walker.core.base.mvvm.model.MvvmBaseModel
import com.walker.core.util.GsonUtils
import com.walker.core.util.Utils
import com.walker.dripstone.fragment.EmptyFragment
import com.walker.dripstone.home.Channel
import com.walker.dripstone.home.HomeChannels
import com.walker.dripstone.home.MockHomeChannels
import kotlinx.coroutines.*

class HomeChannelModel : MvvmBaseModel<HomeChannels, ArrayList<Channel>>(
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
            var fragment: Fragment = EmptyFragment()
            if (TextUtils.equals("101", key)) {
                val summaryProvider = RouteServiceManager.provide(
                    IUiProvider::class.java,
                    IUiProvider.UI_SUMMARY_SERVICE
                )
                fragment = summaryProvider?.getSummaryFragment()!!
            }
            return fragment
        }
    }

    override fun onSuccess(data: HomeChannels?, isFromCache: Boolean) {
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
            val data= withContext(Dispatchers.Default) { mockData() }
            takeIf { data != null }?.also {
                onSuccess(data, false)
            } ?: onFailure(Throwable("数据加载失败"))
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