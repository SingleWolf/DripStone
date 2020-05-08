package com.walker.buildsrc.trace;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class TraceMethodVisitor extends AdviceAdapter {
    private static final String ANNOTATION_TRACK_METHOD = "Lcom/walker/demo/trace/TraceMethod;";
    private static final String METHOD_EVENT_MANAGER = "com/walker/demo/trace/MethodEventManager";
    private final MethodVisitor methodVisitor;
    private final String methodName;

    private boolean needInject;
    private String tag;

    public TraceMethodVisitor(MethodVisitor methodVisitor, int access, String name, String desc) {
        super(Opcodes.ASM6, methodVisitor, access, name, desc);
        this.methodVisitor = methodVisitor;
        this.methodName = name;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(desc, visible);
        System.out.println(String.format("\n\n---------- visitAnnotation desc is : %s ----------\n\n", desc));
        if (desc.equals(ANNOTATION_TRACK_METHOD)) {
            needInject = true;
            return new AnnotationVisitor(Opcodes.ASM6, annotationVisitor) {
                @Override
                public void visit(String name, Object value) {
                    super.visit(name, value);
                    if (name.equals("tag") && value instanceof String) {
                        tag = (String) value;
                        System.out.println(String.format("\n\n----------trace method tag : %s ----------\n\n", tag));
                    }
                }
            };
        }
        return annotationVisitor;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        handleMethodEnter();
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        handleMethodExit();
    }

    private void handleMethodEnter() {
        if (needInject && tag != null) {
            System.out.println(String.format("\n\n---------- %s handleMethodEnter tag : %s ----------\n\n", methodName, tag));
            methodVisitor.visitMethodInsn(INVOKESTATIC, METHOD_EVENT_MANAGER,
                    "getInstance", "()L" + METHOD_EVENT_MANAGER + ";", false);
            methodVisitor.visitLdcInsn(tag);
            methodVisitor.visitLdcInsn(methodName);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, METHOD_EVENT_MANAGER,
                    "notifyMethodEnter", "(Ljava/lang/String;Ljava/lang/String;)V", false);
        }
    }

    private void handleMethodExit() {
        if (needInject && tag != null) {
            System.out.println(String.format("\n\n---------- %s handleMethodExit tag : %s ----------\n\n", methodName, tag));
            methodVisitor.visitMethodInsn(INVOKESTATIC, METHOD_EVENT_MANAGER,
                    "getInstance", "()L" + METHOD_EVENT_MANAGER + ";", false);
            methodVisitor.visitLdcInsn(tag);
            methodVisitor.visitLdcInsn(methodName);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, METHOD_EVENT_MANAGER,
                    "notifyMethodExit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
        }
    }
}
