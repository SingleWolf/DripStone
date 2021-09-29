package com.walker.optimize.group.oom.leakcanary;

public class LeakCanaryHelper {
    private static volatile LeakCanaryHelper sInstance;
    private Watcher refWatcher;

    public static LeakCanaryHelper get() {
        if (sInstance == null) {
            synchronized (LeakCanaryHelper.class) {
                if (sInstance == null) {
                    sInstance = new LeakCanaryHelper();
                }
            }
        }
        return sInstance;
    }

    private LeakCanaryHelper() {
        refWatcher = new Watcher();
    }

    public synchronized void watch(Object watchedReference, String referenceName) {
        refWatcher.watch(watchedReference, referenceName);
    }

    public int getRetainedRefCount() {
        return refWatcher.getRetainedRefCount();
    }
}
