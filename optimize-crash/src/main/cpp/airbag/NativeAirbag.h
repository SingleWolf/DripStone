/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_walker_crash_NativeAirbagHelper */

#ifndef _Included_com_walker_crash_NativeAirbagHelper
#define _Included_com_walker_crash_NativeAirbagHelper
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_walker_crash_NativeAirbagHelper
 * Method:    stringFromJNI
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_walker_crash_NativeAirbagHelper_stringFromJNI
  (JNIEnv *, jclass);

/*
 * Class:     com_walker_crash_NativeAirbagHelper
 * Method:    openNativeAirbag
 * Signature: (ILjava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_walker_crash_NativeAirbagHelper_openNativeAirbag
  (JNIEnv *, jclass, jint, jstring, jstring);

/*
 * Class:     com_walker_crash_NativeAirbagHelper
 * Method:    testNativeCrash_1
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_walker_crash_NativeAirbagHelper_testNativeCrash_11
  (JNIEnv *, jclass);

/*
 * Class:     com_walker_crash_NativeAirbagHelper
 * Method:    testNativeCrash_2
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_walker_crash_NativeAirbagHelper_testNativeCrash_12
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif


struct NativeAirBagConfig {
    int signal;
    std::string soName;
    std::string backtrace;
};
