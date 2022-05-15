package com.walker.study.skin;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.walker.core.base.mvc.BaseFragment;
import com.walker.core.log.LogHelper;
import com.walker.study.R;
import com.walker.study.skin.core.SkinManager;

import java.io.File;

public class SkinFragment extends BaseFragment implements View.OnClickListener {

    public static final String KEY_ID = "key_study_skin_fragment";
    public static String SKIN_BLUE_PATH = "";
    public static String SKIN_BLACK_PATH = "";

    public static Fragment instance() {
        return new SkinFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SKIN_BLUE_PATH = context.getExternalFilesDir("DripStone").getAbsolutePath() + File.separator + "skin" + File.separator + "skin-blue.apk";
        SKIN_BLACK_PATH = context.getExternalFilesDir("DripStone").getAbsolutePath() + File.separator + "skin" + File.separator + "skin-black.apk";
    }

    @Override
    protected void buildView(View baseView, Bundle savedInstanceState) {
        baseView.findViewById(R.id.btnLoad).setOnClickListener(this);
        baseView.findViewById(R.id.btnBlue).setOnClickListener(this);
        baseView.findViewById(R.id.btnBlack).setOnClickListener(this);
        baseView.findViewById(R.id.btnYellow).setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_study_skin;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnLoad) {
            LogHelper.get().i("SkinFragment", "skin-load");
            SkinWorker.INSTANCE.transactSkin();
        } else if (id == R.id.btnBlue) {
            LogHelper.get().i("SkinFragment", "skin-blue");
            SkinManager.getInstance().loadSkin(SKIN_BLUE_PATH);
        } else if (id == R.id.btnBlack) {
            LogHelper.get().i("SkinFragment", "skin-black");
            SkinManager.getInstance().loadSkin(SKIN_BLACK_PATH);
        } else if (id == R.id.btnYellow) {
            LogHelper.get().i("SkinFragment", "skin-yellow");
            SkinManager.getInstance().loadSkin(null);
        }
    }
}
