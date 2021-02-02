package com.walker.optimize.group.anr;

import android.os.FileObserver;
import android.util.Log;

import androidx.annotation.Nullable;

import com.walker.core.log.LogHelper;

public class ANRFileObserver extends FileObserver {
    private static final String TAG = "ANRFileObserver";

    public ANRFileObserver(String path) {//data/anr/
        super(path);
    }

    public ANRFileObserver(String path, int mask) {
        super(path, mask);
    }

    @Override
    public void onEvent(int event, @Nullable String path) {
        switch (event) {
            case FileObserver.ACCESS://文件被访问
                LogHelper.get().i(TAG, "ACCESS: " + path);
                break;
            case FileObserver.ATTRIB://文件属性被修改，如 chmod、chown、touch 等
                LogHelper.get().i(TAG, "ATTRIB: " + path);
                break;
            case FileObserver.CLOSE_NOWRITE://不可写文件被 close
                LogHelper.get().i(TAG, "CLOSE_NOWRITE: " + path);
                break;
            case FileObserver.CLOSE_WRITE://可写文件被 close
                LogHelper.get().i(TAG, "CLOSE_WRITE: " + path);
                break;
            case FileObserver.CREATE://创建新文件
                LogHelper.get().i(TAG, "CREATE: " + path);
                break;
            case FileObserver.DELETE:// 文件被删除，如 rm
                LogHelper.get().i(TAG, "DELETE: " + path);
                break;
            case FileObserver.DELETE_SELF:// 自删除，即一个可执行文件在执行时删除自己
                LogHelper.get().i(TAG, "DELETE_SELF: " + path);
                break;
            case FileObserver.MODIFY://文件被修改
                LogHelper.get().i(TAG, "MODIFY: " + path);
                break;
            case FileObserver.MOVE_SELF://自移动，即一个可执行文件在执行时移动自己
                LogHelper.get().i(TAG, "MOVE_SELF: " + path);
                break;
            case FileObserver.MOVED_FROM://文件被移走，如 mv
                LogHelper.get().i(TAG, "MOVED_FROM: " + path);
                break;
            case FileObserver.MOVED_TO://文件被移来，如 mv、cp
                LogHelper.get().i(TAG, "MOVED_TO: " + path);
                break;
            case FileObserver.OPEN://文件被 open
                LogHelper.get().i(TAG, "OPEN: " + path);
                break;
            default:
                //CLOSE ： 文件被关闭，等同于(IN_CLOSE_WRITE | IN_CLOSE_NOWRITE)
                //ALL_EVENTS ： 包括上面的所有事件
                LogHelper.get().i(TAG, "DEFAULT(" + event + "): " + path);
                break;
        }
    }
}
