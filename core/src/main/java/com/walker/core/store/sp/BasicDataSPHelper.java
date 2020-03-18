package com.walker.core.store.sp;

public class BasicDataSPHelper extends BasePreferences {
    private static final String BASIC_DATA_PREFERENCE_FILE_NAME = "network_api_module_basic_data_preference";
    private static BasicDataSPHelper sInstance;

    public static BasicDataSPHelper get() {
        if (sInstance == null) {
            synchronized (BasicDataSPHelper.class) {
                if (sInstance == null) {
                    sInstance = new BasicDataSPHelper();
                }
            }
        }
        return sInstance;
    }

    @Override
    protected String getSPFileName() {
        return BASIC_DATA_PREFERENCE_FILE_NAME;
    }
}
