package com.walker.common.log;

import android.content.Context;

import com.walker.core.log.BaseLogger;

import java.io.File;

public class DefaultLogger extends BaseLogger {

    public DefaultLogger(Context context) {
        super(context);
    }

    @Override
    protected String getLogDirPath() {
        return mContext.getExternalFilesDir("DripStone").getAbsolutePath() + File.separator + "log";
    }

    @Override
    protected long getMaxFileSize() {
        return 5 * 1024 * 1024;
    }
}
