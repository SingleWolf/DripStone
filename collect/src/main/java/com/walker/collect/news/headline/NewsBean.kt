package com.walker.collect.news.headline

data class Channel(var channelId: String="", var channelName: String="")

data class NewsChannels(val channelList:ArrayList<Channel>)