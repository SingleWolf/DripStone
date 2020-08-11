package com.walker.ui.router;

import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.walker.common.router.IUiRouter;
import com.walker.ui.summary.SummaryFragment;

@AutoService(IUiRouter.class)
public class UiRouterImpl implements IUiRouter {

    @Override
    public Fragment getSummaryFragment() {
        return new SummaryFragment();
    }
}
