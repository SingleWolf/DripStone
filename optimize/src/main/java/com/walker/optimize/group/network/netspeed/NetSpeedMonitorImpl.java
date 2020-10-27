package com.walker.optimize.group.network.netspeed;

import com.walker.optimize.group.network.connectionclass.ConnectionClassManager;
import com.walker.optimize.group.network.connectionclass.ConnectionQuality;
import com.walker.optimize.group.network.connectionclass.DeviceBandwidthSampler;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author Walker
 * @Date 2020/10/26 2:10 PM
 * @Summary 网速监测实现者
 */
public class NetSpeedMonitorImpl implements INetSpeedMonitor {
    private OnNetSpeedListener mObserver;
    private ConnectionClassManager mConnectionClassManager;
    private DeviceBandwidthSampler mDeviceBandwidthSampler;
    private ConnectionChangedListener mListener;
    private AtomicBoolean mIsStarting = new AtomicBoolean(false);

    public NetSpeedMonitorImpl(OnNetSpeedListener observer) {
        mObserver = observer;
        mConnectionClassManager = ConnectionClassManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        mListener = new ConnectionChangedListener();
    }


    @Override
    public void start() {
        if (mIsStarting.get()) {
            return;
        }
        mDeviceBandwidthSampler.startSampling();
        mIsStarting.compareAndSet(false, true);
    }

    @Override
    public void stop() {
        mDeviceBandwidthSampler.stopSampling();
        mIsStarting.set(false);
    }

    @Override
    public void register() {
        mConnectionClassManager.register(mListener);
    }

    @Override
    public void unregister() {
        mConnectionClassManager.remove(mListener);
    }

    private class ConnectionChangedListener
            implements ConnectionClassManager.ConnectionClassStateChangeListener {
        @Override
        public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
            mConnectionClassManager.reset();
            if (mObserver != null && bandwidthState != null) {
                mObserver.onNetChanged(genNetSpeed(bandwidthState));
            }
        }
    }

    private NetSpeed genNetSpeed(ConnectionQuality bandwidthState) {
        NetSpeed netSpeed;
        switch (bandwidthState) {
            case EXCELLENT:
                netSpeed = NetSpeed.EXCELLENT;
                break;
            case GOOD:
                netSpeed = NetSpeed.GOOD;
                break;
            case MODERATE:
                netSpeed = NetSpeed.MODERATE;
                break;
            case POOR:
                netSpeed = NetSpeed.POOR;
                break;
            default:
                netSpeed = NetSpeed.UNKNOWN;
                break;
        }
        return netSpeed;
    }
}
