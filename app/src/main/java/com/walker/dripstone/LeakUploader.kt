package com.walker.dripstone

import leakcanary.DefaultOnHeapAnalyzedListener
import leakcanary.OnHeapAnalyzedListener
import shark.HeapAnalysis

class LeakUploader : OnHeapAnalyzedListener {

    private val defaultListener = DefaultOnHeapAnalyzedListener.create()

    override fun onHeapAnalyzed(heapAnalysis: HeapAnalysis) {
        //heapAnalysis.heapDumpFile.copyTo()
        defaultListener.onHeapAnalyzed(heapAnalysis)
    }
}