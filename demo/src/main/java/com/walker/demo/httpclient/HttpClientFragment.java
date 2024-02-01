package com.walker.demo.httpclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.walker.core.base.mvc.BaseFragment;
import com.walker.core.log.LogHelper;
import com.walker.demo.R;

public class HttpClientFragment extends BaseFragment {
    public static final String KEY_ID = "key_demo_httpclient_fragment";
    private SamplingHandler handler;
    private HandlerThread handlerThread;

    public static HttpClientFragment instance() {
        return new HttpClientFragment();
    }

    @Override
    protected void buildView(View baseView, Bundle savedInstanceState) {
        baseView.findViewById(R.id.tvStart).setOnClickListener(view -> {
            startRequest();
        });
        baseView.findViewById(R.id.tvStop).setOnClickListener(view -> {
            stopRequest();
        });
        baseView.findViewById(R.id.tvGC).setOnClickListener(view -> {
            Runtime.getRuntime().gc();
        });

        handlerThread = new HandlerThread("HttpClient-Thread");
        handlerThread.start();
        handler = new SamplingHandler(handlerThread.getLooper());
    }

    private void startRequest() {
        LogHelper.get().i("HttpClientFragment","startRequest()");
        handler.startSamplingThread();
    }

    private void stopRequest() {
        LogHelper.get().i("HttpClientFragment","stopRequest()");
        handler.stopSamplingThread();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_demo_httpclient;
    }

    private class SamplingHandler extends Handler {
        /**
         * Time between polls in ms.
         */
        static final long SAMPLE_TIME = 100;

        static private final int MSG_START = 1;

        public SamplingHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START:
                    addRequest();
                    sendEmptyMessageDelayed(MSG_START, SAMPLE_TIME);
                    break;
                default:
                    break;
            }
        }


        public void startSamplingThread() {
            sendEmptyMessage(SamplingHandler.MSG_START);
        }

        public void stopSamplingThread() {
            removeMessages(SamplingHandler.MSG_START);
        }
    }

    private void addRequest() {
        RequestUtils.postRequest();
    }
}
