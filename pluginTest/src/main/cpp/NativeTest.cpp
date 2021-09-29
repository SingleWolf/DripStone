//
// Created by walker on 2021/9/29.
//

#include "com_example_plugintest_NativeTest.h"

#include <string>
#include <jni.h>

using namespace std;

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_plugintest_NativeTest_getInfoFromNative(JNIEnv *env, jobject) {
    string content = "你好，我来插件中的C++代码";
    return env->NewStringUTF(content.c_str());
};