package com.walker.aop_optimize_plugin.plugins.legalBitmap

import com.walker.aop_optimize_plugin.base.BaseTransform
import com.walker.aop_optimize_plugin.utils.Log
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

class LegalBitmapTransform(private val config: LegalBitmapConfig) : BaseTransform() {

    private companion object {

        private const val ImageViewClass = "android/widget/ImageView"

    }

    override fun modifyClass(byteArray: ByteArray): ByteArray {
        val classReader = ClassReader(byteArray)
        val className = classReader.className
        val superName = classReader.superName
        return if (className != config.formatMonitorImageViewClass && superName == ImageViewClass) {
            Log.log("[Child]className: $className superName: $superName")
            val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
            val classVisitor = object : ClassVisitor(Opcodes.ASM6, classWriter) {
                override fun visit(
                    version: Int,
                    access: Int,
                    name: String?,
                    signature: String?,
                    superName: String?,
                    interfaces: Array<out String>?
                ) {
                    super.visit(
                        version,
                        access,
                        name,
                        signature,
                        config.formatMonitorImageViewClass,
                        interfaces
                    )
                }
            }
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
            classWriter.toByteArray()
        } else {
            byteArray
        }
    }

}