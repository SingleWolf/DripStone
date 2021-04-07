package com.walker.dripstone

import com.walker.core.log.LogHelper
import com.walker.core.store.storage.StorageHelper
import com.walker.core.util.DateTimeUtils
import leakcanary.DefaultOnHeapAnalyzedListener
import leakcanary.OnHeapAnalyzedListener
import shark.HeapAnalysis
import java.io.File

class LeakUploader : OnHeapAnalyzedListener {

    private val defaultListener = DefaultOnHeapAnalyzedListener.create()

    override fun onHeapAnalyzed(heapAnalysis: HeapAnalysis) {
        try {
            val hprofParentPath = "${StorageHelper.getRootPath()}/leakCanary"
            val file = File(hprofParentPath)
            if (!file.exists()) {
                file.mkdirs()
            }
            val hprofPath = "${hprofParentPath}/${DateTimeUtils.getNormalDate()}.hprof"
            heapAnalysis.heapDumpFile.copyTo(File(hprofPath), true)
        } catch (e: Exception) {
            LogHelper.get().e("LeakUploader", e.toString())
        } finally {
            defaultListener.onHeapAnalyzed(heapAnalysis)
        }
    }
}