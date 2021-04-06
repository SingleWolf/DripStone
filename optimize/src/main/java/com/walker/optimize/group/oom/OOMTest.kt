package com.walker.optimize.group.oom

import android.system.Os.socket
import android.system.OsConstants.AF_INET
import android.system.OsConstants.SOCK_DGRAM
import android.util.Log
import java.lang.Thread.sleep

class OOMTest {
    private var mHeap = arrayListOf<ByteArray>()

    private val increaseFDRunnable = Runnable {
        readFD()
    }
    private val emptyRunnable = Runnable {
        try {
            Thread.sleep(Long.MAX_VALUE)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun readFD() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            try {
                for (i in 0 until 10 * 1024) {
                    var iSock = socket(AF_INET, SOCK_DGRAM, 0)
                    Log.d("OOMTest", "iSock is ${iSock.valid()}")
                }
            } catch (e: Exception) {
                Log.e("OOMTest", e.toString())
            }
        }
    }

    /**
     * 添加FD数目模拟测试
     *
     * @param count FD数
     */
    fun testIncreaseFD(count: Int) {
        if (count < 1) {
            return
        }
        Thread(increaseFDRunnable).start()

    }

    /**
     * 增加线程数测试
     *
     * @param count 线程数
     */
    fun testIncreaseThread(count: Int) {
        if (count < 1) {
            return
        }
        for (i in 0 until count) {
            Thread(emptyRunnable).start()
        }
    }

    /**
     * 占用堆内存测试
     *
     * @param count 数量
     */
    fun testAllocateJavaHeap(count: Int) {
        if (count < 1) {
            return
        }
        val bytes = ByteArray(count)
        mHeap.add(bytes)
    }

    /**
     * GC和释放堆内存测试
     */
    fun testGCAndDeallocate() {
        mHeap.clear()
        mHeap = arrayListOf()
        //System.gc(）仅仅是通知系统在合适时间进行一次垃圾回收。并不能保证一定执行
        Runtime.getRuntime().gc()
        sleep(100)
        System.runFinalization()
    }
}