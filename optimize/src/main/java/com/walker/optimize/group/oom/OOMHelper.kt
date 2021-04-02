package com.walker.optimize.group.oom

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.walker.common.BaseApplication
import com.walker.core.log.LogHelper
import com.walker.core.store.storage.StorageHelper
import com.walker.core.util.DateTimeUtils
import java.io.File
import java.io.RandomAccessFile

/**
 * @Author Walker
 * @Date 2021/4/1 3:30 PM
 * @Summary OOM辅助类
 */
class OOMHelper {
    companion object {
        const val TAG = "OOMHelper"
        private var sInstance: OOMHelper? = null
        const val UNIT_M = (1024 * 1024).toFloat()

        fun get(): OOMHelper {
            if (sInstance == null) {
                synchronized(OOMHelper::class.java) {
                    if (sInstance == null) {
                        sInstance = OOMHelper()
                    }
                }
            }
            return sInstance!!
        }
    }

    private constructor()

    fun genDumpFile(): Boolean {
        val hprofParentPath =
            "${StorageHelper.getRootPath()}/dump_gc"
        return genDumpFile(hprofParentPath)
    }

    private fun genDumpFile(filePath: String): Boolean {
        var result = false
        try {
            val file = File(filePath)
            if (!file.exists()) {
                file.mkdirs()
            }
            val hprofPath = "${filePath}/${DateTimeUtils.getNormalDate()}.hprof"
            android.os.Debug.dumpHprofData(hprofPath)
            result = true
            LogHelper.get().d(TAG, "genDumpFile successful")
        } catch (e: Exception) {
            LogHelper.get().e(TAG, e.message)
        }
        return result
    }

    fun getAppMemorySize2M(): Int {
        val activityManager =
            BaseApplication.context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //以m为单位
        return activityManager?.memoryClass
    }

    /**
     * 获取OOM相关统计详细信息
     *
     * @param pid 进程id
     * @return 详细信息
     */
    fun listStatisticsInfo(pid: Int): String? {
        val stringBuilder = StringBuilder()
        stringBuilder.append("\r\n")
        stringBuilder.append(" FD count: ").append(getFdCount(pid)).append("\r\n")
        stringBuilder.append(getMemoryInfo())
        stringBuilder.append("# proc status #\r\n")
        stringBuilder.append(getProcStatus(pid)).append("\r\n")
        stringBuilder.append("# proc limit #\r\n")
        stringBuilder.append(getProcLimit(pid))
        stringBuilder.append("\r\n")
        return stringBuilder.toString()
    }

    /**
     * 获取OOM相关统计的大概信息
     *
     * @param pid 进程id
     * @return 大概信息
     */
    fun listStatisticsSummary(pid: Int): String? {
        val stringBuilder = StringBuilder()
        stringBuilder.append("\r\n")
        stringBuilder.append(" FD count: ").append(getFdCount(pid)).append("\r\n")
        stringBuilder.append(getMemoryInfo())
        stringBuilder.append("\r\n")
        return stringBuilder.toString()
    }


    /**
     * 获取文件描述符(fd)数目
     *
     * @param pid 进程id
     * @return 文件fd数
     */
    private fun getFdCount(pid: Int): Int {
        val filePath = String.format("/proc/%d/fd", pid)
        val fdFile = File(filePath)
        val files = fdFile.listFiles()
        return files?.size ?: -1
    }

    /**
     * 获取内存使用信息
     *
     * @return 内存使用详情
     */
    fun getMemoryInfo(): String? {
        val stringBuilder = StringBuilder()
        stringBuilder.append("MaxJavaHeap : ").append(Runtime.getRuntime().maxMemory() / UNIT_M)
            .append(" MB\r\n")
        stringBuilder.append("UsedJavaHeap  : ").append(
            (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / UNIT_M
        ).append(" MB\r\n")
        return stringBuilder.toString()
    }

    /**
     * 获取进程相关限制信息
     *
     * @param pid 进程id
     * @return 限制信息
     */
    private fun getProcLimit(pid: Int): String? {
        val filePath = String.format("/proc/%d/limits", pid)
        return getFileContent(filePath)
    }

    /**
     * 获取进程当前状态信息
     *
     * @param pid 进程id
     * @return 当前状态信息
     */
    private fun getProcStatus(pid: Int): String? {
        val filePath = String.format("/proc/%d/status", pid)
        return getFileContent(filePath)
    }

    /**
     * 获取文件内容
     *
     * @param path 文件路径
     * @return 文件内容
     */
    private fun getFileContent(path: String): String? {
        if (TextUtils.isEmpty(path)) {
            return "path is null"
        }
        var content: String
        try {
            val randomAccessFile = RandomAccessFile(path, "r")
            val stringBuilder = StringBuilder()
            var s: String?
            while (randomAccessFile.readLine().also { s = it } != null) {
                stringBuilder.append(s).append("\r\n")
            }
            content = stringBuilder.toString()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            content = e.message.toString()
        }
        return content
    }
}