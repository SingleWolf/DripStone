package com.walker.dripstone.home.headline

import com.walker.core.base.mvvm.viewmodel.MvvmBaseViewModel
import com.walker.dripstone.home.Channel

class HeadlineViewModel:MvvmBaseViewModel<HomeChannelModel,Channel>{
    constructor() : super(){
        model = HomeChannelModel()
        model.register(this)
        model.getCachedDataAndLoad()
    }
}