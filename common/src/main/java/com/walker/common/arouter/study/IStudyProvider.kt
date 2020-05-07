package com.walker.common.arouter.study

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.template.IProvider

interface IStudyProvider : IProvider {
    companion object {
        const val STUDY_ROUTER = "/study/"
        const val STUDY_SUMMARY_SERVICE = STUDY_ROUTER + "study_summary"
    }

    fun getSummaryFragment(): Fragment?
}