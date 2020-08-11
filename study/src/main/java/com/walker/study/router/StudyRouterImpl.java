package com.walker.study.router;

import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.walker.common.router.IStudyRouter;
import com.walker.study.summary.SummaryFragment;

@AutoService({IStudyRouter.class})
public class StudyRouterImpl implements IStudyRouter {
    @Override
    public Fragment getSummaryFragment() {
        return new SummaryFragment();
    }
}
