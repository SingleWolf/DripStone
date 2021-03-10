package com.walker.demo.summary

import android.util.Log
import com.walker.common.view.titleview.TitleViewViewModel
import com.walker.core.base.mvvm.customview.BaseCustomViewModel
import com.walker.core.base.mvvm.model.MvvmBaseModel
import com.walker.core.util.GsonUtils
import com.walker.demo.MockSummaryData
import kotlinx.coroutines.*

class SummaryModel(private val viewModelScope: CoroutineScope) :
    MvvmBaseModel<SummaryListBean, ArrayList<BaseCustomViewModel>>(
        SummaryListBean::class.java,
        true,
        "pref_key_ui_summary",
        "", 1
    ) {
    override fun onSuccess(data: SummaryListBean?, isFromCache: Boolean) {
        data?.let {
            val baseViewModels = ArrayList<BaseCustomViewModel>()
            for (source in it.summaryList) {
                val viewModel = TitleViewViewModel()
                viewModel.key = source.key
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
        viewModelScope.launch {
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
            val pageNum = if (isRefresh) {
                1
            } else {
                pageNumber
            }
            jsonData = MockSummaryData.get().listSummary(pageNum)
            data = GsonUtils.fromLocalJson(jsonData, SummaryListBean::class.java)
            Log.i("mockData", jsonData)
        }
        return data
    }

}
