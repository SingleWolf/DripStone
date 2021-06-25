package com.walker.collect.summary

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class SummaryViewModel : ViewModel(), LifecycleObserver {
    var dataList: MutableLiveData<SummaryListBean> = MutableLiveData()
    val summaryModel by lazy { SummaryModel() }

    fun getData() {
        viewModelScope.launch {
            val data = summaryModel.load()
            data?.also {
                dataList.value = data!!
                dataList.postValue(dataList.value)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        dataList.postValue(dataList.value)
    }
}