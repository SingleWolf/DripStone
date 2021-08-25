package com.walker.common.aop;

import android.util.Log;

//import org.objectweb.asm.ClassReader;
//import org.objectweb.asm.Opcodes;
//import org.objectweb.asm.tree.ClassNode;
//import org.objectweb.asm.tree.FieldNode;
//import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Author  : walker
 * Date    : 2021/8/24  9:31 上午
 * Email   : feitianwumu@163.com
 * Summary : ASM工具类
 */
public class AsmUtils {
    private static final String TAG = "AsmUtils";

//    /**
//     * 可以通过Class 对象，找到其在AS中具体的路径
//     *
//     * @param clazz Class对象
//     * @return class类的路径
//     */
//    public static String getClassFilePath(Class clazz) {
//        //String buildDir = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
//        String buildDir="/Users/walker/开发/Android/DripStone/usercenter/build/intermediates/javac/debug/classes/";
//        String fileName = clazz.getSimpleName() + ".class";
//        File file = new File(buildDir + clazz.getPackage().getName().replaceAll("[.]", "/") + "/", fileName);
//        return file.getAbsolutePath();
//    }
//
//    /**
//     * 获取clazz中包含的所有方法以及字段
//     *
//     * @param clazz 目标class
//     */
//    public static void showClassNodeInfo(Class clazz) {
//        try {
//            String clazzFilePath = getClassFilePath(clazz);
//            ClassReader classReader = new ClassReader(new FileInputStream(clazzFilePath));
//            ClassNode classNode = new ClassNode(Opcodes.ASM5);
//            classReader.accept(classNode, 0);
//
//            List<MethodNode> methods = classNode.methods;
//            List<FieldNode> fields = classNode.fields;
//            Log.d(TAG, clazz.getName() + " 's bytecode :");
//            Log.d(TAG, "methods:");
//            for (MethodNode methodNode : methods) {
//                Log.d(TAG, methodNode.name + ", " + methodNode.desc);
//            }
//
//            Log.d(TAG, "fields:");
//            for (FieldNode fieldNode : fields) {
//                Log.d(TAG, fieldNode.name + ", " + fieldNode.desc);
//            }
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//
//    }
}
