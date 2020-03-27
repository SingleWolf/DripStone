package com.walker.collect.news.headline

import com.walker.core.base.mvvm.viewmodel.MvvmBaseViewModel

class HeadlineViewModel:MvvmBaseViewModel<NewsChannelModel, Channel>{
    constructor() : super(){
        model = NewsChannelModel()
        model.register(this)
        model.getCachedDataAndLoad()
    }
}