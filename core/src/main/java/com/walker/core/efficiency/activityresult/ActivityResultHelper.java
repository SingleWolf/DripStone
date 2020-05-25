package com.walker.core.efficiency.activityresult;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * @Author Walker
 * @Date 2019-10-23 14:06
 * @Summary activityResult辅助器
 */
public class ActivityResultHelper {
    private static final String TAG = "ActivityResultHelper";
    private Context mContext;
    private RouterFragment mRouterFragment;

    public static ActivityResultHelper init(AppCompatActivity activity) {
        return new ActivityResultHelper(activity);
    }

    private ActivityResultHelper(AppCompatActivity activity) {
        mContext = activity;
        mRouterFragment = getRouterFragment(activity);
    }

    private RouterFragment getRouterFragment(AppCompatActivity activity) {
        RouterFragment routerFragment = findRouterFragment(activity);
        if (routerFragment == null) {
            routerFragment = RouterFragment.newInstance();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(routerFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return routerFragment;
    }

    private RouterFragment findRouterFragment(AppCompatActivity activity) {
        return (RouterFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
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