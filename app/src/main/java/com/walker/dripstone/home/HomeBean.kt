package com.walker.dripstone.home

data class Channel(var channelId: String="", var channelName: String="", var uri: String="")

data class HomeChannels(val channelList:ArrayList<Channel>)