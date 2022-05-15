package com.walker.buildsrc.optimize.thread

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class ThreadMethodVisitor extends AdviceAdapter {
    public static final String GOAL_CLAZZ = "com/walker/optimize/group/oom/thread/ShadowThread";
    private String methodName;
    private String className;
    // 标识是否遇到了 new 指令
    private boolean find = false;

    protected ThreadMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc, String className) {
        super(api, mv, access, name, desc);
        this.className = className;
        this.methodName = name;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
    }

    @Override
    void visitTypeInsn(int opcode, String type) {
        if (opcode == Opcodes.NEW && "java/lang/Thread".equals(type)) {
            // 遇到 new 指令
            System.out.println("\n\n ThreadMethodVisitor => 遇到 new 指令 className=" + className + "\n\n");
            find = true;
            mv.visitTypeInsn(Opcodes.NEW, GOAL_CLAZZ);
            return;
        }
        super.visitTypeInsn(opcode, type);
    }

    @Override
    void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        //需要排查 ShadowThread 自己
        if ("java/lang/Thread".equals(owner) && !className.equals(GOAL_CLAZZ) && opcode == Opcodes.INVOKESPECIAL && find) {
            find = false;
            mv.visitMethodInsn(opcode, GOAL_CLAZZ, name, desc, itf);
            System.out.println(String.format("\n\n ThreadMethodVisitor => className:%s, method:%s, name:%s , desc:%s\n\n", className, methodName, name, desc));
            return;
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

}

