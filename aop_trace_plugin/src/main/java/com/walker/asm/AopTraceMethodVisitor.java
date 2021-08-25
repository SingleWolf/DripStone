package com.walker.asm;

import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class AopTraceMethodVisitor extends MethodVisitor {
    private static final String ANNOTATION_AOP_TRACE = "Lcom/walker/common/aop/AopTrace;";
    private MethodVisitor methodVisitor;
    private String methodName;
    private String pointClassName;
    private boolean needInject;

    public AopTraceMethodVisitor(MethodVisitor mv, String name) {
        super(Opcodes.ASM6, mv);
        methodVisitor = mv;
        methodName = name;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(desc, visible);
        if (desc.equals(ANNOTATION_AOP_TRACE)) {
            System.out.println(">>> AopTraceMethodVisitor" + "\n");
            return new AnnotationVisitor(Opcodes.ASM6, annotationVisitor) {

                @Override
                public void visit(String name, Object value) {
                    super.visit(name, value);
                    System.out.println(">>> visit name=" + name + "\n");
                    if (name.equals("point") && value instanceof Type) {
                        pointClassName = ((Type) value).getInternalName();
                        needInject = true;
                    }
                }
            };
        }
        return annotationVisitor;
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        return super.visitParameterAnnotation(parameter, desc, visible);
    }

    @Override
    public void visitCode() {
        methodVisitor.visitCode();
        if (!needInject) {
            return;
        }
        methodVisitor.visitTypeInsn(NEW, pointClassName);
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, pointClassName, "<init>", "()V", false);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, pointClassName, "onBeforeMethod", "()V", false);
    }

    @Override
    public void visitInsn(int opcode) {
        if (!needInject) {
            methodVisitor.visitInsn(opcode);
            return;
        }
        if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
            methodVisitor.visitTypeInsn(NEW, pointClassName);
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, pointClassName, "<init>", "()V", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, pointClassName, "onAfterMethod", "()V", false);
        }
        methodVisitor.visitInsn(opcode);
    }
}
