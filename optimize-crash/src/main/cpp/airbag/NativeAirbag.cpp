
#include <jni.h>
#include <string>
#include <android/log.h>
#include "NativeAirbag.h"
#include "unwind-utils.h"

#define LOG_TAG            "native_airbag"
#define LOG(fmt, ...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##__VA_ARGS__)
#define SIGNAL_CRASH_STACK_SIZE (1024 * 128)

static struct sigaction old;

static NativeAirBagConfig airBagConfig;

static void sig_handler(int sig, struct siginfo *info, void *ptr) {
    auto stackTrace = getStackTraceWhenCrash();
//    if (sig == airBagConfig.signal &&
//        stackTrace.find(airBagConfig.soName) != std::string::npos &&
//        stackTrace.find(airBagConfig.backtrace) != std::string::npos) {
    if (sig == airBagConfig.signal) {
        const char *stackInfo = stackTrace.data();
        LOG("异常信号已捕获:sig=%d \n errorInfo= %s", sig, stackInfo);
    } else {
        LOG("异常信号交给原有信号处理器处理");
        sigaction(sig, &old, nullptr);
        raise(sig);
    }
}

void handle_exception(JNIEnv *env) {
    LOG("native air bag init failed");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_walker_crash_NativeAirbagHelper_stringFromJNI(JNIEnv *env, jclass clazz) {
    std::string hello = "Hello from C++ NativeAirbag";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_walker_crash_NativeAirbagHelper_openNativeAirbag(JNIEnv *env, jclass clazz, jint signal,
                                                          jstring so_name, jstring backtrace) {
    airBagConfig.signal = signal;
    airBagConfig.soName = env->GetStringUTFChars(so_name, 0);
    airBagConfig.backtrace = env->GetStringUTFChars(backtrace, 0);
    do {
        stack_t ss;
        if (nullptr == (ss.ss_sp = calloc(1, SIGNAL_CRASH_STACK_SIZE))) {
            handle_exception(env);
            break;
        }
        ss.ss_size = SIGNAL_CRASH_STACK_SIZE;
        ss.ss_flags = 0;
        if (0 != sigaltstack(&ss, nullptr)) {
            handle_exception(env);
            break;
        }
        struct sigaction sigc;
        sigc.sa_sigaction = sig_handler;
        sigemptyset(&sigc.sa_mask);
        // 推荐采用SA_RESTART 虽然不是所有系统调用都支持，被中断后重新启动，但是能覆盖大部分
        sigc.sa_flags = SA_SIGINFO | SA_ONSTACK | SA_RESTART;
        int flag = sigaction(signal, &sigc, &old);
        if (flag == -1) {
            handle_exception(env);
            break;
        }
    } while (false);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_walker_crash_NativeAirbagHelper_testNativeCrash_11(JNIEnv *env, jclass clazz) {
    LOG("已保护Native崩溃");
    raise(SIGSEGV);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_walker_crash_NativeAirbagHelper_testNativeCrash_12(JNIEnv *env, jclass clazz) {
    LOG("未保护Native崩溃");
    raise(SIGABRT);
}

