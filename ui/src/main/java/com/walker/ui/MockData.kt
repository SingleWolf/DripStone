package com.walker.ui

import com.walker.core.log.LogHelper
import com.walker.core.util.GsonUtils
import com.walker.ui.group.colorlayout.ColorLayoutFragment
import com.walker.ui.group.floatlayout.FloatLayoutFragment
import com.walker.ui.group.goodfish.GoodFishFragment
import com.walker.ui.group.photoview.PhotoViewFragment
import com.walker.ui.group.recyclerview.ceiling.StarFragment
import com.walker.ui.group.recyclerview.draworder.DrawOrderFragment
import com.walker.ui.group.recyclerview.slidecard.SlideCardFragment
import com.walker.ui.group.recyclerview.weblist.WebListAdapter
import com.walker.ui.group.recyclerview.weblist.WebListFragment
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
        const val PAGE_SIZE = 20

        fun get(): MockSummaryData {
            return Holder.instance
        }
    }

    fun generateSummary(): List<Summary> {
        var summaryList = mutableListOf<Summary>()
        summaryList.run {
            Summary().run {
                key = ""
                title = "优化PagerView高度"
                uri = ""
                desc = ""
                add(this)
                LogHelper.get().d("generateSummary", title, true)
            }

            Summary().run {
                key = FloatLayoutFragment.KEY_ID
                title = "流布局FloatLayout"
                uri = ""
                desc = ""
                add(this)
                LogHelper.get().d("generateSummary", title, true)
            }

            Summary().run {
                key = ColorLayoutFragment.KEY_ID
                title = "渐变色布局"
                uri = ""
                desc = ""
                add(this)
                LogHelper.get().d("generateSummary", title, true)
            }

            Summary().run {
                key = GoodFishFragment.KEY_ID
                title = "灵动锦鲤"
                uri = ""
                desc = ""
                add(this)
                LogHelper.get().d("generateSummary", title, true)
            }

            Summary().run {
                key = StarFragment.KEY_ID
                title = "RecyclerView吸顶效果"
                uri = ""
                desc = ""
                add(this)
                LogHelper.get().d("generateSummary", title, true)
            }

            Summary().run {
                key = DrawOrderFragment.KEY_ID
                title = "自定义绘制顺序"
                uri = ""
                desc = ""
                add(this)
                LogHelper.get().d("generateSummary", title, true)
            }

            Summary().run {
                key = SlideCardFragment.KEY_ID
                title = "滑动切换卡片"
                uri = ""
                desc = ""
                add(this)
                LogHelper.get().d("generateSummary", title, true)
            }


            Summary().run {
                key = ""
                title = "嵌套滑动机制"
                uri = ""
                desc = ""
                add(this)
                LogHelper.get().d("generateSummary", title, true)
            }

            Summary().run {
                key = PhotoViewFragment.KEY_ID
                title = "PhotoView图片查看"
                uri = ""
                desc = ""
                add(this)
                LogHelper.get().d("generateSummary", title, true)
            }

            Summary().run {
                key = WebListFragment.KEY_ID
                title = "web列表"
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