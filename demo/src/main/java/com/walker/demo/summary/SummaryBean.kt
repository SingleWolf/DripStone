package com.walker.demo.summary

data class Summary(
    var key: String = "",
    var title: String = "",
    var uri: String = "",
    var desc: String = ""
)

data class SummaryListBean(
    val summaryList: List<Summary>,
    val allNum: Int,
    val currentPage: Int
)