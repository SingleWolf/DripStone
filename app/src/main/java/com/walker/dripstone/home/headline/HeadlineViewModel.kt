package com.walker.dripstone.home.headline

import android.os.Looper
import androidx.lifecycle.viewModelScope
import com.walker.core.base.mvvm.viewmodel.MvvmBaseViewModel
import com.walker.dripstone.home.Channel

class HeadlineViewModel : MvvmBaseViewModel<HomeChannelModel, Channel> {
    constructor() : super() {
        model = HomeChannelModel(viewModelScope)
        model.register(this)
        //使用IdleHandler进行懒加载
        Looper.myQueue().addIdleHandler {
            model.getCachedDataAndLoad()
            false
        }
    }
}