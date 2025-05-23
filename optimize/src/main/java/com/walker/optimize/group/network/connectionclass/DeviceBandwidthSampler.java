/*
 *  Copyright (c) 2015, Facebook, Inc.
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree. An additional grant
 *  of patent rights can be found in the PATENTS file in the same directory.
 *
 */

package com.walker.optimize.group.network.connectionclass;

import android.net.TrafficStats;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;

/**
 * Class used to read from TrafficStats periodically, in order to determine a ConnectionClass.
 */
public class DeviceBandwidthSampler {
    private static final String TAG="NetSpeedHelper";
    /**
     * The DownloadBandwidthManager that keeps track of the moving average and ConnectionClass.
     */
    private final ConnectionClassManager mConnectionClassManager;

    private AtomicBoolean mSamplingCounter;

    private SamplingHandler mHandler;
    private HandlerThread mThread;

    private long mLastTimeReading;
    private static long sPreviousBytes = -1;

    // Singleton.
    private static class DeviceBandwidthSamplerHolder {
        public static final DeviceBandwidthSampler instance =
                new DeviceBandwidthSampler(ConnectionClassManager.getInstance());
    }

    /**
     * Retrieval method for the DeviceBandwidthSampler singleton.
     *
     * @return The singleton instance of DeviceBandwidthSampler.
     */
    @Nonnull
    public static DeviceBandwidthSampler getInstance() {
        return DeviceBandwidthSamplerHolder.instance;
    }

    private DeviceBandwidthSampler(
            ConnectionClassManager connectionClassManager) {
        mConnectionClassManager = connectionClassManager;
        mSamplingCounter = new AtomicBoolean(false);
        mThread = new HandlerThread("ParseThread");
        mThread.start();
        mHandler = new SamplingHandler(mThread.getLooper());
    }

    /**
     * Method call to start sampling for download bandwidth.
     */
    public void startSampling() {
        if (!mSamplingCounter.get()) {
            Log.d(TAG,"start()");
            mSamplingCounter.compareAndSet(false,true);
            mHandler.startSamplingThread();
            mLastTimeReading = SystemClock.elapsedRealtime();
        }
    }

    /**
     * Finish sampling and prevent further changes to the
     * ConnectionClass until another timer is started.
     */
    public void stopSampling() {
        if (mSamplingCounter.get()) {
            Log.d(TAG,"stop()");
            mSamplingCounter.compareAndSet(true,false);
            mHandler.stopSamplingThread();
            addFinalSample();
        }
    }

    /**
     * Method for polling for the change in total bytes since last update and
     * adding it to the BandwidthManager.
     */
    protected void addSample() {
        long newBytes = TrafficStats.getTotalRxBytes();
        long byteDiff = newBytes - sPreviousBytes;
        Log.d(TAG, "计算处理网络速度:" + "newBytes=" + newBytes + " byteDiff=" + byteDiff);
        if (sPreviousBytes >= 0) {
            synchronized (this) {
                long curTimeReading = SystemClock.elapsedRealtime();
                mConnectionClassManager.addBandwidth(byteDiff, curTimeReading - mLastTimeReading);

                mLastTimeReading = curTimeReading;
            }
        }
        sPreviousBytes = newBytes;
    }

    /**
     * Resets previously read byte count after recording a sample, so that
     * we don't count bytes downloaded in between sampling sessions.
     */
    protected void addFinalSample() {
        addSample();
        sPreviousBytes = -1;
    }

    /**
     * @return True if there are still threads which are sampling, false otherwise.
     */
    public boolean isSampling() {
        return (mSamplingCounter.get());
    }

    private class SamplingHandler extends Handler {
        /**
         * Time between polls in ms.
         */
        static final long SAMPLE_TIME = 1000;

        static private final int MSG_START = 1;

        public SamplingHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START:
                    addSample();
                    sendEmptyMessageDelayed(MSG_START, SAMPLE_TIME);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown what=" + msg.what);
            }
        }


        public void startSamplingThread() {
            sendEmptyMessage(SamplingHandler.MSG_START);
        }

        public void stopSamplingThread() {
            removeMessages(SamplingHandler.MSG_START);
        }
    }
}