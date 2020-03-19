package com.walker.dripstone.home.headline

import androidx.fragment.app.Fragment
import com.walker.core.base.mvvm.model.MvvmBaseModel
import com.walker.core.util.GsonUtils
import com.walker.dripstone.fragment.EmptyFragment
import com.walker.dripstone.home.Channel
import com.walker.dripstone.home.HomeChannels

class HomeChannelModel : MvvmBaseModel<HomeChannels, ArrayList<Channel>>(
    HomeChannels::class.java
) {

    companion object {
        const val CHANNEL_DATA = "{\n" +
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
                "            \"channelName\": \"排雷\",\n" +
                "            \"uri\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"channelId\": \"106\",\n" +
                "            \"channelName\": \"推荐\",\n" +
                "            \"uri\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"channelId\": \"107\",\n" +
                "            \"channelName\": \"工具\",\n" +
                "            \"uri\": \"\"\n" +
                "        }\n" +
                "    ]\n" +
                "}"

        fun createFragment(key: String): Fragment {
            return EmptyFragment()
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
        val data = mockData()
        if (data == null) {
            onFailure(Throwable("数据加载失败"))
        } else {
            onSuccess(data, false)
        }
    }

    fun mockData(): HomeChannels? {
        return GsonUtils.fromLocalJson(CHANNEL_DATA, HomeChannels::class.java)
    }
}