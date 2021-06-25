package com.walker.collect.summary

import com.walker.collect.MockSummaryData
import com.walker.common.view.titleview.TitleViewViewModel
import com.walker.core.base.mvvm.customview.BaseCustomViewModel
import com.walker.core.base.mvvm.model.MvvmBaseModel
import com.walker.core.log.LogHelper
import com.walker.core.util.GsonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SummaryModel {

    suspend fun load(): SummaryListBean? {
        return mockData()
    }

    private suspend fun mockData(): SummaryListBean? {
        var data: SummaryListBean? = null
        var jsonData: String
        withContext(Dispatchers.IO) {
            jsonData = MockSummaryData.get().listSummary()
            LogHelper.get().i("mockData","data=$jsonData")
            data = GsonUtils.fromLocalJson(jsonData, SummaryListBean::class.java)
        }
        return data
    }

}
