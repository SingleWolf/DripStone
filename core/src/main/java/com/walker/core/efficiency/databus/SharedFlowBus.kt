package com.walker.core.efficiency.databus

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.ConcurrentHashMap

/**
 * Author  : walker
 * Date    : 2022/1/24  10:37 上午
 * Email   : feitianwumu@163.com
 * Summary : 基于Flow实现数据总线
 */
object SharedFlowBus {
    private var events = ConcurrentHashMap<Any, MutableSharedFlow<Any>>()
    private var stickyEvents = ConcurrentHashMap<Any, MutableSharedFlow<Any>>()

    fun <T> with(objectKey: Class<T>): MutableSharedFlow<T> {
        if (!events.containsKey(objectKey)) {
            events[objectKey] = MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
        }
        return events[objectKey] as MutableSharedFlow<T>
    }

    fun <T> withSticky(objectKey: Class<T>): MutableSharedFlow<T> {
        if (!stickyEvents.containsKey(objectKey)) {
            stickyEvents[objectKey] = MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
        }
        return stickyEvents[objectKey] as MutableSharedFlow<T>
    }

    fun <T> on(objectKey: Class<T>): LiveData<T> {
        return with(objectKey).asLiveData()
    }

    fun <T> onSticky(objectKey: Class<T>): LiveData<T> {
        return withSticky(objectKey).asLiveData()
    }
}