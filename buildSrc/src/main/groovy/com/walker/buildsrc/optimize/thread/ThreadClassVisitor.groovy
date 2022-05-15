package com.walker.buildsrc.optimize.thread

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class ThreadClassVisitor extends ClassVisitor {
    private String className;

    ThreadClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM6, cv)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.className = name
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions)
        return new ThreadMethodVisitor(Opcodes.ASM6, methodVisitor, access, name, desc, this.className)
    }
}
