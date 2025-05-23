package com.walker.study

import com.walker.core.log.LogHelper
import com.walker.core.util.GsonUtils
import com.walker.study.annotation.InjectFragment
import com.walker.study.hotfix.HotfixFragment
import com.walker.study.plugin.HookPluginFragment
import com.walker.study.skin.SkinFragment
import com.walker.study.summary.Summary
import com.walker.study.summary.SummaryListBean
import com.walker.study.thread.ThreadFragment
import com.walker.study.webview.WebviewUseFragment

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
        summaryList.run {
            Summary().run {
                key = InjectFragment.KEY_ID
                title = "注解与反射"
                uri = ""
                desc = ""
                add(this)
                LogHelper.get().d("generateSummary", title, true)
            }
            Summary().run {
                key = ThreadFragment.KEY_ID
                title = "线程与并发"
                uri = ""
                desc = ""
                add(this)
                LogHelper.get().d("generateSummary", title, true)
            }
            Summary().run {
                key = HotfixFragment.KEY_ID
                title = "dex插桩实现热修复"
                uri = ""
                desc = ""
                add(this)
                LogHelper.get().d("generateSummary", title, true)
            }
            Summary().run {
                key = HookPluginFragment.KEY_ID
                title = "Hook和插件化"
                uri = ""
                desc = ""
                add(this)
                LogHelper.get().d("generateSummary", title, true)
            }
            Summary().run {
                key = WebviewUseFragment.KEY_ID
                title = "跨进程Webview"
                uri = ""
                desc = ""
                add(this)
                LogHelper.get().d("generateSummary", title, true)
            }
            Summary().run {
                key = SkinFragment.KEY_ID
                title = "插件化换肤"
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