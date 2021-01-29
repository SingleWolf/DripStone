package com.walker.core.efficiency.databus;

import androidx.lifecycle.MutableLiveData;

import com.walker.core.util.StringBuilderUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Walker
 * @Date 2021/1/29 2:44 PM
 * @Summary 数据总线
 */
public class LiveDataBus {
    //存放订阅者
    private Map<String, MutableLiveData<Object>> bus;
    private static LiveDataBus liveDataBus = new LiveDataBus();

    private LiveDataBus() {
        bus = new HashMap();
    }
    public static LiveDataBus getInstance() {
        return liveDataBus;
    }
    //注册订阅者
    public synchronized <T> MutableLiveData<T> with(String key, Class<T> type) {
        if(!bus.containsKey(key)){
            bus.put(key,new MutableLiveData<Object>());
        }
        return (MutableLiveData<T>)bus.get(key);
    }

    //注册订阅者（去除粘性事件）
    public synchronized <T> MutableLiveData<T> withNotStick(String key, Class<T> type) {
        String keyNotStick= StringBuilderUtils.plicePlus(key,"_not_stick");
        if(!bus.containsKey(keyNotStick)){
            bus.put(keyNotStick,new BusMutableLiveData<>());
        }
        return (BusMutableLiveData<T>)bus.get(keyNotStick);
    }
}
