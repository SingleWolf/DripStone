package com.walker.collect.summary

import com.walker.collect.MockSummaryData
import com.walker.common.view.titleview.TitleViewViewModel
import com.walker.core.base.mvvm.customview.BaseCustomViewModel
import com.walker.core.base.mvvm.model.MvvmBaseModel
import com.walker.core.util.GsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SummaryModel :
    MvvmBaseModel<SummaryListBean, ArrayList<BaseCustomViewModel>>(
        SummaryListBean::class.java) {
    override fun onSuccess(data: SummaryListBean?, isFromCache: Boolean) {
        data?.let {
            val baseViewModels = ArrayList<BaseCustomViewModel>()
            for (source in it.summaryList) {
                val viewModel = TitleViewViewModel()
                viewModel.jumpUri = source.uri
                viewModel.title = source.title
                baseViewModels.add(viewModel)
            }
            loadSuccess(it, baseViewModels, isFromCache)
        }
    }

    override fun onFailure(e: Throwable?) {
        e?.printStackTrace()
        loadFail(e?.message)
    }

    override fun refresh() {
        isRefresh = true
        load()
    }

    fun loadNexPage() {
        isRefresh = false
        load()
    }

    override fun load() {
        runBlocking {
            val data = withContext(Dispatchers.Default) { mockData() }
            takeIf { data != null }?.also {
                onSuccess(data, false)
            } ?: onFailure(Throwable("数据加载失败"))
        }
    }

    suspend fun mockData(): SummaryListBean? {
        var data: SummaryListBean? = null
        var jsonData: String
        withContext(Dispatchers.IO) {
            jsonData = MockSummaryData.get().listSummary()
            data = GsonUtils.fromLocalJson(jsonData, SummaryListBean::class.java)
        }
        return data
    }

}
