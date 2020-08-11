package com.walker.demo.router;

import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.walker.common.router.IDemoRouter;
import com.walker.demo.summary.SummaryFragment;

import org.jetbrains.annotations.Nullable;

@AutoService(IDemoRouter.class)
public class DemoRouterImpl implements IDemoRouter {
    @Nullable
    @Override
    public Fragment getSummaryFragment() {
        return new SummaryFragment();
    }
}
