package com.walker.dripstone.home

data class Channel(val channelId: String, val channelName: String, val uri: String?)

data class HomeChannels(val channelList:ArrayList<Channel>)