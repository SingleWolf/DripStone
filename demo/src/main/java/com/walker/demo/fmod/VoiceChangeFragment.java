package com.walker.demo.fmod;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.walker.core.base.mvc.BaseFragment;
import com.walker.core.log.LogHelper;
import com.walker.demo.R;

public class VoiceChangeFragment extends BaseFragment implements View.OnClickListener {


    public static final String KEY_ID = "key_demo_voice_change_fragment";

    private String path = "file:///android_asset/tea.m4a";

    public static Fragment instance() {
        return new VoiceChangeFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        VoiceChangeHelper.get().init(getHoldContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VoiceChangeHelper.get().unInit();
    }

    @Override
    protected void buildView(View baseView, Bundle savedInstanceState) {
        baseView.findViewById(R.id.btn_normal).setOnClickListener(this);
        baseView.findViewById(R.id.btn_luoli).setOnClickListener(this);
        baseView.findViewById(R.id.btn_dashu).setOnClickListener(this);
        baseView.findViewById(R.id.btn_jingsong).setOnClickListener(this);
        baseView.findViewById(R.id.btn_gaoguai).setOnClickListener(this);
        baseView.findViewById(R.id.btn_kongling).setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_demo_voice_change;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_normal) {
            LogHelper.get().d("VoiceChangeFragment","点击【正常声音】");
            VoiceChangeHelper.get().changeVoice(path, VoiceChangeHelper.MODE_NORMAL);
        } else if (id == R.id.btn_luoli) {
            LogHelper.get().d("VoiceChangeFragment","点击【萝莉声音】");
            VoiceChangeHelper.get().changeVoice(path, VoiceChangeHelper.MODE_LUOLI);
        } else if (id == R.id.btn_dashu) {
            LogHelper.get().d("VoiceChangeFragment","点击【大叔声音】");
            VoiceChangeHelper.get().changeVoice(path, VoiceChangeHelper.MODE_DASHU);
        } else if (id == R.id.btn_jingsong) {
            LogHelper.get().d("VoiceChangeFragment","点击【惊悚声音】");
            VoiceChangeHelper.get().changeVoice(path, VoiceChangeHelper.MODE_JINGSONG);
        } else if (id == R.id.btn_gaoguai) {
            LogHelper.get().d("VoiceChangeFragment","点击【搞怪声音】");
            VoiceChangeHelper.get().changeVoice(path, VoiceChangeHelper.MODE_GAOGUAI);
        } else if (id == R.id.btn_kongling) {
            LogHelper.get().d("VoiceChangeFragment","点击【空灵声音】");
            VoiceChangeHelper.get().changeVoice(path, VoiceChangeHelper.MODE_KONGLING);
        }
    }
}
