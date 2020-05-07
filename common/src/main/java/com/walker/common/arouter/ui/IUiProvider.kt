package com.walker.common.arouter.ui

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.template.IProvider

interface IUiProvider :IProvider{
    companion object{
        const val UI_ROUTER="/ui/"
        const val UI_SUMMARY_SERVICE= UI_ROUTER+"ui_summary"
    }

    fun getSummaryFragment():Fragment?
}