package com.walker.collect.summary

data class Summary(
    var key: String = "",
    var title: String = "",
    var uri: String = "",
    var desc: String = "",
    var imageUrl: String = ""
)

data class SummaryListBean(
    val bannerList: List<Summary>?
)