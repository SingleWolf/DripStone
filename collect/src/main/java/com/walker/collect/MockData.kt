package com.walker.collect

import com.walker.collect.cook.cooklist.CookListActivity
import com.walker.collect.news.headline.Channel
import com.walker.collect.news.headline.NewsSummaryFragment
import com.walker.collect.summary.Summary
import com.walker.collect.summary.SummaryListBean
import com.walker.core.util.GsonUtils

class MockSummaryData private constructor() {
    var dataPool: List<Summary>

    init {
        dataPool = this.generateBanner()
    }

    private object Holder {
        val instance = MockSummaryData()
    }

    companion object {
        const val ORIGINAL_KEY = 100

        const val IMAGE_URL_NEWS =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.vjshi.com%2F2016-01-27%2F2015-6dc681e22794b6df133e5514323313ed%2F00002.jpg%3Fx-oss-process%3Dstyle%2Fwatermark&refer=http%3A%2F%2Fpic.vjshi.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1627540680&t=6fe14f9ee4c38233164f071a154da3ca"
        const val IMAGE_URL_FOOD =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2018-09-19%2F5ba1c5466ecf4.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1627541006&t=eac76db71fd4aecef8848b337a48c198"
        const val IMAGE_URL_SMILE =
            "https://img1.baidu.com/it/u=1756422723,3273404372&fm=26&fmt=auto&gp=0.jpg"

        fun get(): MockSummaryData {
            return Holder.instance
        }
    }

    fun generateBanner(): List<Summary> {
        var index = ORIGINAL_KEY
        var summaryList = mutableListOf<Summary>()
        summaryList.run {
            Summary().run {
                key = NewsSummaryFragment.channel_id
                title = "新闻头条"
                uri = ""
                desc = ""
                imageUrl = IMAGE_URL_NEWS
                add(this)
            }
            Summary().run {
                key = CookListActivity.CHANNEL_ID
                title = "菜谱大全"
                uri = ""
                desc = ""
                imageUrl = IMAGE_URL_FOOD
                add(this)
            }
            Summary().run {
                key = "${++index}"
                title = "开心一刻"
                uri = ""
                desc = ""
                imageUrl = IMAGE_URL_SMILE
                add(this)
            }
        }

        return summaryList
    }

    fun listSummary(): String {
        var summaryListBean: SummaryListBean
        var bannerList = mutableListOf<Summary>()
        bannerList.addAll(dataPool)
        summaryListBean = SummaryListBean(bannerList)
        return GsonUtils.toJson(summaryListBean)
    }
}

class MockNewsData {
    companion object {
        fun getChannels(): String {
            var channelList = mutableListOf<Channel>()
            channelList.let {
                Channel().run {
                    channelId = "top"
                    channelName = "头条"
                    it.add(this)
                }
                Channel().run {
                    channelId = "shehui"
                    channelName = "社会"
                    it.add(this)
                }
                Channel().run {
                    channelId = "guonei"
                    channelName = "国内"
                    it.add(this)
                }
                Channel().run {
                    channelId = "guoji"
                    channelName = "国际"
                    it.add(this)
                }
                Channel().run {
                    channelId = "yule"
                    channelName = "娱乐"
                    it.add(this)
                }
                Channel().run {
                    channelId = "tiyu"
                    channelName = "体育"
                    it.add(this)
                }
                Channel().run {
                    channelId = "junshi"
                    channelName = "军事"
                    it.add(this)
                }
                Channel().run {
                    channelId = "keji"
                    channelName = "科技"
                    it.add(this)
                }
                Channel().run {
                    channelId = "caijing"
                    channelName = "财经"
                    it.add(this)
                }
                Channel().run {
                    channelId = "shishang"
                    channelName = "时尚"
                    it.add(this)
                }
            }

            val jsonMap = HashMap<String, List<Channel>>()
            jsonMap["channelList"] = channelList

            return GsonUtils.toJson(jsonMap)
        }
    }
}