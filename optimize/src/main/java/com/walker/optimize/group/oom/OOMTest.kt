package com.walker.optimize.group.oom

import android.os.Process
import java.io.BufferedReader
import java.io.FileReader

class OOMTest {
    private var mHeap = arrayListOf<ByteArray>()
    private val increaseFDRunnable = Runnable {
        for (i in 0 until 40 * 1000) {
            readFD()
        }
    }
    private val emptyRunnable = Runnable {
        try {
            Thread.sleep(Long.MAX_VALUE)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun readFD() {
        try {
            BufferedReader(FileReader("/proc/" + Process.myPid() + "/status"))
            Thread.sleep(Long.MAX_VALUE)
        } catch (e: Exception) {
            e.printStackTrace()
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
        System.gc()
    }
}