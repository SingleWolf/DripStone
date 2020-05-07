package com.walker.study.router;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.walker.common.arouter.study.IStudyProvider;
import com.walker.study.summary.SummaryFragment;

import org.jetbrains.annotations.Nullable;

@Route(path = IStudyProvider.STUDY_SUMMARY_SERVICE)
public class StudyProvider implements IStudyProvider {
    @Nullable
    @Override
    public Fragment getSummaryFragment() {
        return new SummaryFragment();
    }

    @Override
    public void init(Context context) {

    }
}
