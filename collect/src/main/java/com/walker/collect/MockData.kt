package com.walker.collect

import com.walker.collect.news.headline.Channel
import com.walker.collect.news.headline.NewsSummaryFragment
import com.walker.collect.summary.Summary
import com.walker.collect.summary.SummaryListBean
import com.walker.core.util.GsonUtils

class MockSummaryData private constructor() {
    var dataPool: List<Summary>

    init {
        dataPool = this.generateSummary()
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

    fun generateSummary(): List<Summary> {
        var index = ORIGINAL_KEY
        var summaryList = mutableListOf<Summary>()
        summaryList.run {
            Summary().run {
                key = NewsSummaryFragment.channel_id
                title = "新闻头条"
                uri = ""
                desc = ""
                add(this)
            }
            Summary().run {
                key = "${++index}"
                title = "开心一笑"
                uri = ""
                desc = ""
                add(this)
            }
        }

        return summaryList
    }

    fun listSummary(): String {
        var summaryListBean: SummaryListBean
        var summaryList = mutableListOf<Summary>()
        summaryList.addAll(dataPool)
        summaryListBean = SummaryListBean(summaryList, dataPool.size)
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