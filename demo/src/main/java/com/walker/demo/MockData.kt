package com.walker.demo

import com.walker.core.log.LogHelper
import com.walker.core.util.GsonUtils
import com.walker.demo.a2b.AppA2bFragment
import com.walker.demo.install.AppInstallFragment
import com.walker.demo.fmod.VoiceChangeFragment
import com.walker.demo.location.LocationFragment
import com.walker.demo.paging3.RepoFragment
import com.walker.demo.shortcut.ShortcutFragment
import com.walker.demo.summary.Summary
import com.walker.demo.summary.SummaryListBean

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
        var summaryList = mutableListOf<Summary>()
        var keyIndex: Int = ORIGINAL_KEY
        Summary().run {
            key = RepoFragment.KEY_ID
            title = "Paging3"
            uri = ""
            desc = ""
            summaryList.add(this)
        }

        Summary().run {
            key = VoiceChangeFragment.KEY_ID
            title = "fmod实现变声"
            uri = ""
            desc = ""
            summaryList.add(this)
        }

        Summary().run {
            key = AppA2bFragment.KEY_ID
            title = "跨应用跳转"
            uri = ""
            desc = ""
            summaryList.add(this)
        }

        Summary().run {
            key = AppInstallFragment.KEY_ID
            title = "应用安装"
            uri = ""
            desc = ""
            summaryList.add(this)
        }

        Summary().run {
            key = ShortcutFragment.KEY_ID
            title = "快捷方式"
            uri = ""
            desc = ""
            summaryList.add(this)
        }

        Summary().run {
            key = LocationFragment.KEY_ID
            title = "定位功能"
            uri = ""
            desc = ""
            summaryList.add(this)
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