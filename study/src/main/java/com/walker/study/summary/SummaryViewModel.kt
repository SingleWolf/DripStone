package com.walker.study.summary

import androidx.lifecycle.viewModelScope
import com.walker.core.base.mvvm.customview.BaseCustomViewModel
import com.walker.core.base.mvvm.viewmodel.MvvmBaseViewModel

class SummaryViewModel : MvvmBaseViewModel<SummaryModel, ArrayList<BaseCustomViewModel>> {
    constructor() : super() {
        model = SummaryModel(viewModelScope)
        model.register(this)
        model.getCachedDataAndLoad()
    }

    fun tryToLoadNextPage() {
        model.loadNexPage()
    }
}