package com.walker.collect.router;

import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.walker.collect.summary.SummaryFragment;
import com.walker.common.router.ICollectRouter;

import org.jetbrains.annotations.Nullable;

@AutoService(ICollectRouter.class)
public class CollectRouterImpl implements ICollectRouter {
    @Nullable
    @Override
    public Fragment getHomeFragment() {
        return new SummaryFragment();
    }
}
