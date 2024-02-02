package com.walker.demo.screenshot;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.walker.core.util.ToastUtils;

public class ScreenshotObserver extends ContentObserver {
    private static final String TAG = "ScreenshotObserver";
    private static final String MEDIA_EXTERNAL_CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString();

    private Context mContext;

    private ScreenShotFloat screenShotFloat;

    private ScreenShotFloatAdapter floatAdapter;

    public ScreenshotObserver(Handler handler, Context context) {
        super(handler);
        mContext = context;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);

        // 判断是否为截图事件
        if (uri.toString().matches(MEDIA_EXTERNAL_CONTENT_URI + "/[0-9]+")) {
            Log.d(TAG, "Detected screenshot event");

            // 在这里处理截图事件，例如保存截图文件、发送通知等
            // 获取截图文件路径
            String filePath = getFilePathFromContentUri(mContext, uri);

            // 处理截图文件
            handleScreenshot(filePath);
        }
    }

    private String getFilePathFromContentUri(Context context, Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return cursor.getString(columnIndex);
        }
        return null;
    }

    private void handleScreenshot(String filePath) {
        // 处理截图文件
        Log.d(TAG, "Screenshot file path: " + filePath);
        // 在这里可以保存截图文件、发送通知等
        if (!TextUtils.isEmpty(filePath)) {
            showFloatView(filePath);
        }
    }

    private void showFloatView(String filePath) {
        if (floatAdapter == null) {
            floatAdapter = new ScreenShotFloatAdapter();
        }
        if (screenShotFloat == null) {
            screenShotFloat = new ScreenShotFloat(mContext);
            screenShotFloat.setAdapter(floatAdapter);
        }

        if (screenShotFloat.isShow()) {
            floatAdapter.setData(filePath);
            floatAdapter.notifyDataChanged();
        } else {
            floatAdapter.setData(filePath);
            screenShotFloat.show();
        }
    }
}
