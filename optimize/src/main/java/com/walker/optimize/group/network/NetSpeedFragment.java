package com.walker.optimize.group.network;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.walker.core.base.mvc.BaseFragment;
import com.walker.core.ui.loadsir.EmptyCallback;
import com.walker.core.ui.loadsir.LoadingCallback;
import com.walker.optimize.R;
import com.walker.optimize.databinding.FragmentOptimizeNetSpeedBinding;
import com.walker.optimize.group.network.netspeed.NetSpeed;
import com.walker.optimize.group.network.netspeed.NetSpeedHelper;
import com.walker.optimize.group.network.netspeed.OnNetSpeedListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Author Walker
 * @Date 2020/10/23 4:32 PM
 * @Summary 实时监测网速
 */
public class NetSpeedFragment extends BaseFragment {

    public static final String KEY_ID = "key_optimize_net_speed_fragment";
    private FragmentOptimizeNetSpeedBinding mDataBinding;
    private String mURL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1603447810422&di=60d8653480e90097f31f81b4505a292d&imgtype=0&src=http%3A%2F%2Ft7.baidu.com%2Fit%2Fu%3D3616242789%2C1098670747%26fm%3D79%26app%3D86%26f%3DJPEG%3Fw%3D900%26h%3D1350";
    private int mTries = 0;
    private NetSpeed mNetSpeed = NetSpeed.UNKNOWN;
    private LoadService mLoadService;

    public static Fragment instance() {
        return new NetSpeedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mDataBinding.getRoot();
    }

    @Override
    protected void buildView(View baseView, Bundle savedInstanceState) {
        NetSpeedHelper.get().register();
        NetSpeedHelper.get().setOnNetSpeedListener(mListener);
        mDataBinding.btnStart.setOnClickListener((v) -> {
            new DownloadImage().execute(mURL);
        });
        mLoadService = LoadSir.getDefault().register(mDataBinding.layoutContent, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                new DownloadImage().execute(mURL);
            }
        });
        mLoadService.showSuccess();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_optimize_net_speed;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NetSpeedHelper.get().unregister();
    }

    private OnNetSpeedListener mListener = new OnNetSpeedListener() {
        @Override
        public void onNetChanged(NetSpeed netSpeed) {
            if (netSpeed != NetSpeed.UNKNOWN) {
                mTries = 0;
            }
            mNetSpeed = netSpeed;
            mDataBinding.setViewModel(mNetSpeed);
        }
    };

    private class DownloadImage extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            mLoadService.showCallback(LoadingCallback.class);
            NetSpeedHelper.get().start();
        }

        @Override
        protected Void doInBackground(String... url) {
            String imageURL = url[0];
            try {
                // Open a stream to download the image from our URL.
                URLConnection connection = new URL(imageURL).openConnection();
                connection.setUseCaches(false);
                connection.connect();
                InputStream input = connection.getInputStream();
                try {
                    byte[] buffer = new byte[1024];

                    // Do some busy waiting while the stream is open.
                    while (input.read(buffer) != -1) {
                    }
                } finally {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            NetSpeedHelper.get().stop();
            mLoadService.showSuccess();
            if (mNetSpeed == NetSpeed.UNKNOWN) {
                if (mTries < 10) {
                    mTries++;
                    Log.d("NetSpeedHelper", "try and times = " + mTries);
                    new DownloadImage().execute(mURL);
                }
            }
        }
    }
}
