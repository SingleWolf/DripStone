package com.walker.study.retrofit.test;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.walker.core.base.mvc.BaseFragment;
import com.walker.study.R;
import com.walker.study.retrofit.MyRetrofit;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyRetrofitFragment extends BaseFragment {
    private TextView mTvContent;

    public static Fragment instance() {
        return new MyRetrofitFragment();
    }

    @Override
    protected void buildView(View baseView, Bundle savedInstanceState) {
        mTvContent = baseView.findViewById(R.id.tvContent);
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        baseView.findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartNetwork();
            }
        });
    }

    private void onStartNetwork() {
        String baseUrl = "http://v.juhe.cn/";
        String news_key = "1f98e1e9e106fa40cf2f4ced41680b99";
        String channelId = "top";
        MyRetrofit myRetrofit = new MyRetrofit.Builder().baseUrl(baseUrl).build();
        myRetrofit.create(NewsApiInterface.class).getNewsList(news_key, channelId).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mTvContent.post(() -> {
                    mTvContent.setText(e.toString());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                mTvContent.post(() -> {
                    mTvContent.setText(result);
                });
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_study_my_retrofit;
    }
}
