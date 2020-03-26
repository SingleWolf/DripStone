package com.walker.core.efficiency.activityresult;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

import java.util.Random;

/**
 * @Author Walker
 * @Date 2019-10-23 14:09
 * @Summary 空的fragment实现路由转换
 */
public class RouterFragment extends Fragment {

    private SparseArray<ActivityResultHelper.Callback> mCallbacks = new SparseArray<>();
    private Random mCodeGenerator = new Random();

    public RouterFragment() {}

    public static RouterFragment newInstance() {
        return new RouterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startActivityForResult(Intent intent, ActivityResultHelper.Callback callback) {
        int requestCode = makeRequestCode();
        mCallbacks.put(requestCode, callback);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 随机生成唯一的requestCode，最多尝试10次
     *
     * @return
     */
    private int makeRequestCode() {
        int requestCode;
        int tryCount = 0;
        do {
            requestCode = mCodeGenerator.nextInt(0x0000FFFF);
            tryCount++;
        } while (mCallbacks.indexOfKey(requestCode) >= 0 && tryCount < 10);
        return requestCode;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultHelper.Callback callback = mCallbacks.get(requestCode);
        mCallbacks.remove(requestCode);
        if (callback != null) {
            callback.onActivityResult(resultCode, data);
        }
    }
}
