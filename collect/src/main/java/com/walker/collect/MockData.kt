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
                imageUrl =
                    "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20171122%2Fedbb7db8b84d4d96b29dea3055e1e0bb.jpeg&refer=http%3A%2F%2F5b0988e595225.cdn.sohucs.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622187613&t=a946085df1557af8a66a678b8cf3bb0e"
                add(this)
            }
            Summary().run {
                key = CookListActivity.CHANNEL_ID
                title = "菜谱大全"
                uri = ""
                desc = ""
                imageUrl =
                    "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fnews.eastday.com%2Fimages%2Fthumbnailimg%2Fmonth_1610%2F201610010403098090.jpg&refer=http%3A%2F%2Fnews.eastday.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622188163&t=f009e64593b3aee9fcbc8ec40bf87709"
                add(this)
            }
            Summary().run {
                key = "${++index}"
                title = "开心一刻"
                uri = ""
                desc = ""
                imageUrl =
                    "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimage6.pinlue.com%2Fwechat%2Fimg_png%2Fhqbfnpsh6MptziaFcLdaKvWGlibKwtXCyZwcIGzqDibqj1bViav0cHxA8FOicyQna44SSmIK3cavcX5sJppQugXNsTA%2F0.png&refer=http%3A%2F%2Fimage6.pinlue.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622187987&t=9daef2b5017c5b8b9fad19b364b81188"
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