package com.walker.dripstone.home

import com.walker.core.util.GsonUtils

class MockHomeChannels {
    companion object {
        const val ORIGINAL_KEY = 100

        fun getChannels(): String {
            var key= ORIGINAL_KEY
            var channelList = mutableListOf<Channel>()
            channelList.let {
                Channel().run {
                    channelId="${key}"
                    channelName="学习"
                    it.add(this)
                }
                Channel().run {
                    channelId="${++key}"
                    channelName="UI"
                    it.add(this)
                }
                Channel().run {
                    channelId="${++key}"
                    channelName="优化"
                    it.add(this)
                }
                Channel().run {
                    channelId="${++key}"
                    channelName="算法"
                    it.add(this)
                }
                Channel().run {
                    channelId="${++key}"
                    channelName="数据"
                    it.add(this)
                }
                Channel().run {
                    channelId="${++key}"
                    channelName="排雷"
                    it.add(this)
                }
                Channel().run {
                    channelId="${++key}"
                    channelName="推荐"
                    it.add(this)
                }
                Channel().run {
                    channelId="${++key}"
                    channelName="工具"
                    it.add(this)
                }
            }

            val jsonMap= HashMap<String,List<Channel>>()
            jsonMap["channelList"] = channelList

           return GsonUtils.toJson(jsonMap)
        }
    }
}