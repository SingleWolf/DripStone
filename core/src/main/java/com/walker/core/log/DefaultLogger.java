package com.walker.core.log;

import android.content.Context;

public class DefaultLogger extends BaseLogger {

    public DefaultLogger(Context context) {
        super(context);
    }

    @Override
    protected String getLogDirPath() {
        return mContext.getExternalFilesDir("DripStone").getAbsolutePath();
    }

    @Override
    protected long getMaxFileSize() {
        return 1*1024;
    }
}
