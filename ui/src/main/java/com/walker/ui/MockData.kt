package com.walker.ui

import com.walker.core.log.LogHelper
import com.walker.core.util.GsonUtils
import com.walker.ui.group.floatlayout.FloatLayoutFragment
import com.walker.ui.summary.Summary
import com.walker.ui.summary.SummaryListBean

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

        const val PAGE_SIZE = 20

        fun get(): MockSummaryData {
            return Holder.instance
        }
    }

    fun generateSummary(): List<Summary> {
        var index = ORIGINAL_KEY
        var summaryList = mutableListOf<Summary>()
        summaryList.run {
                Summary().run {
                    key = FloatLayoutFragment.KEY_ID
                    title = "流布局FloatLayout"
                    uri = ""
                    desc = ""
                    add(this)

                    LogHelper.get().d("generateSummary", title, true)
                }
        }

        return summaryList
    }

    fun listSummary(pageNum: Int): String {
        var summaryListBean: SummaryListBean
        var summaryList = mutableListOf<Summary>()
        if (pageNum < 1) {
            summaryList.addAll(dataPool)
        } else {
            dataPool?.let {
                val startIndex = (pageNum - 1) * PAGE_SIZE
                val endIndex = pageNum * PAGE_SIZE - 1
                for (index in startIndex..endIndex) {
                    if (0 <= index && index < dataPool.size) {
                        summaryList.add(dataPool[index])
                    }
                }
            }
        }
        LogHelper.get().d("generateSummary", "listSummary success", true)
        summaryListBean = SummaryListBean(summaryList, dataPool.size, pageNum)
        return GsonUtils.toJson(summaryListBean)
    }
}