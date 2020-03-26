package com.walker.core.efficiency.activityresult;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;

/**
 * @Author Walker
 * @Date 2019-10-23 14:06
 * @Summary activityResult辅助器
 */
public class ActivityResultHelper {
    private static final String TAG = "ActivityResultHelper";
    private Context mContext;
    private RouterFragment mRouterFragment;

    public static ActivityResultHelper init(Activity activity) {
        return new ActivityResultHelper(activity);
    }

    private ActivityResultHelper(Activity activity) {
        mContext = activity;
        mRouterFragment = getRouterFragment(activity);
    }

    private RouterFragment getRouterFragment(Activity activity) {
        RouterFragment routerFragment = findRouterFragment(activity);
        if (routerFragment == null) {
            routerFragment = RouterFragment.newInstance();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(routerFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return routerFragment;
    }

    private RouterFragment findRouterFragment(Activity activity) {
        return (RouterFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

    public void startActivityForResult(Class<?> clazz, Callback callback) {
        Intent intent = new Intent(mContext, clazz);
        startActivityForResult(intent, callback);
    }

    public void startActivityForResult(Intent intent, Callback callback) {
        mRouterFragment.startActivityForResult(intent, callback);
    }

    public interface Callback {
        void onActivityResult(int resultCode, Intent data);
    }
}