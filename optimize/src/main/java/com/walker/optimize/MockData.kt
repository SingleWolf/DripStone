package com.walker.optimize

import com.walker.core.log.LogHelper
import com.walker.core.util.GsonUtils
import com.walker.optimize.group.anr.AnrFragment
import com.walker.optimize.group.caton.CatonFragment
import com.walker.optimize.group.network.NetSpeedFragment
import com.walker.optimize.group.trace.TraceMethodFragment
import com.walker.optimize.summary.Summary
import com.walker.optimize.summary.SummaryListBean

class MockSummaryData private constructor() {
    var dataPool: List<Summary>

    init {
        dataPool = this.generateSummary()
    }

    private object Holder {
        val instance = MockSummaryData()
    }

    companion object {
        const val PAGE_SIZE = 20

        fun get(): MockSummaryData {
            return Holder.instance
        }
    }

    fun generateSummary(): List<Summary> {
        var summaryList = mutableListOf<Summary>()
        summaryList.run {
            Summary().run {
                key = TraceMethodFragment.KEY_ID
                title = "TraceMethod"
                uri = ""
                desc = ""
                add(this)
            }
            Summary().run {
                key = NetSpeedFragment.KEY_ID
                title = "实时监测网速"
                uri = ""
                desc = ""
                add(this)
            }
            Summary().run {
                key = CatonFragment.KEY_ID
                title = "卡顿模拟与监测"
                uri = ""
                desc = ""
                add(this)
            }
            Summary().run {
                key = "key_optimize_oom"
                title = "OOM模拟与监测"
                uri = ""
                desc = ""
                add(this)
            }
            Summary().run {
                key = AnrFragment.KEY_ID
                title = "ANR模拟与监测"
                uri = ""
                desc = ""
                add(this)
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