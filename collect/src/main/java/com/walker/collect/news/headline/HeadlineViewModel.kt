package com.walker.collect.news.headline

import android.os.Looper
import com.walker.core.base.mvvm.viewmodel.MvvmBaseViewModel

class HeadlineViewModel:MvvmBaseViewModel<NewsChannelModel, Channel>{
    constructor() : super(){
        model = NewsChannelModel()
        model.register(this)
        //使用IdleHandler进行懒加载
        Looper.myQueue().addIdleHandler {
            model.getCachedDataAndLoad()
            false
        }    }
}