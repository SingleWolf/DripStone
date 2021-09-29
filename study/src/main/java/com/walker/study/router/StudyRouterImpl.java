package com.walker.study.router;

import android.app.Application;
import android.content.Context;

import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.walker.common.router.IStudyRouter;
import com.walker.study.plugin.LoadUtil;
import com.walker.study.skin.core.SkinManager;
import com.walker.study.summary.SummaryFragment;

import org.jetbrains.annotations.NotNull;

@AutoService({IStudyRouter.class})
public class StudyRouterImpl implements IStudyRouter {
    @Override
    public Fragment getSummaryFragment() {
        return new SummaryFragment();
    }

    @Override
    public void loadPlugin(@NotNull Context context, @NotNull String loadPath) {
        LoadUtil.loadPlugin(context,loadPath);
    }

    @Override
    public void initSkin(@NotNull Application application) {
        SkinManager.init(application);
    }
}
