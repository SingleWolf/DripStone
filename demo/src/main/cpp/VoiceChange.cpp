#include <unistd.h>
#include "com_walker_demo_fmod_Voice.h"

using namespace FMOD; // fmod的命名空间

extern "C" JNIEXPORT void JNICALL Java_com_walker_demo_fmod_Voice_voiceChangeNative
        (JNIEnv *env, jobject thiz, jint mode, jstring path) {

    const char *content_ = "默认 播放完毕";

// C认识的字符串
    const char *path_ = env->GetStringUTFChars(path, NULL);

// Java  对象
// C     指针
// Linux 文件

// 音效引擎系统 指针
    System *system = 0;

// 声音 指针
    Sound *sound = 0;

// 通道，音轨，声音在上面跑 跑道 指针
    Channel *channel = 0;

// DSP：digital signal process  == 数字信号处理  指针
    DSP *dsp = 0;

// Java思想 去初始化
// system = xxxx();

// C的思想 初始化
// xxxx(&system);

// TODO 第一步 创建系统
    System_Create(&system);

// TODO 第二步 系统的初始化 参数1：最大音轨数，  参数2：系统初始化标记， 参数3：额外数据
    system->init(32, FMOD_INIT_NORMAL, 0);

// TODO 第三步 创建声音  参数1：路径，  参数2：声音初始化标记， 参数3：额外数据， 参数4：声音指针
    system->createSound(path_, FMOD_DEFAULT, 0, &sound);

// TODO 第四步：播放声音  音轨 声音
// 参数1：声音，  参数2：分组音轨， 参数3：控制， 参数4：通道
    system->playSound(sound, 0, false, &channel);

// 作业：截取字符串 rry  用的二级指针   我需要拿到你的引用 来修改你main函数的 result

// TODO 第五步：增加特效
    switch (mode) {
        case com_walker_demo_fmod_Voice_MODE_NORMAL: // 原生
            content_ = "原生 播放完毕";
            break;
        case com_walker_demo_fmod_Voice_MODE_LUOLI: // 萝莉
            content_ = "萝莉 播放完毕";

// 音调高 -- 萝莉 2.0
// 1.创建DSP类型的Pitch 音调条件
            system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
// 2.设置Pitch音调调节2.0
            dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH, 2.0f);
// 3.添加音效进去 音轨
            channel->addDSP(0, dsp);
            break;
        case com_walker_demo_fmod_Voice_MODE_DASHU: // 大叔
            content_ = "大叔 播放完毕";

// 音调低 -- 大叔 0.7
// 1.创建DSP类型的Pitch 音调条件
            system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
// 2.设置Pitch音调调节2.0
            dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH, 0.7f);
// 3.添加音效进去 音轨
            channel->addDSP(0, dsp);
            break;
        case com_walker_demo_fmod_Voice_MODE_GAOGUAI: // 搞怪
            content_ = "搞怪 小黄人 播放完毕";

// 小黄人声音 频率快

// 从音轨拿 当前 频率
            float mFrequency;
            channel->getFrequency(&mFrequency);

// 修改频率
            channel->setFrequency(mFrequency * 1.5f); // 频率加快  小黄人的声音
            break;
        case com_walker_demo_fmod_Voice_MODE_JINGSONG: // 惊悚
            content_ = "惊悚 播放完毕";

// 惊悚音效：特点： 很多声音的拼接

// TODO 音调低
// 音调低 -- 大叔 0.7
// 1.创建DSP类型的Pitch 音调条件
            system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
// 2.设置Pitch音调调节2.0
            dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH, 0.7f);
// 3.添加音效进去 音轨
            channel->addDSP(0, dsp); // 第一个音轨

// TODO 搞点回声
// 回音 ECHO
            system->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
            dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY, 200); // 回音 延时    to 5000.  Default = 500.
            dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK, 10); // 回音 衰减度 Default = 50   0 完全衰减了
            channel->addDSP(1, dsp); // 第二个音轨

// TODO 颤抖
// Tremolo 颤抖音 正常5    非常颤抖  20
            system->createDSPByType(FMOD_DSP_TYPE_TREMOLO, &dsp);
            dsp->setParameterFloat(FMOD_DSP_TREMOLO_FREQUENCY, 20); // 非常颤抖
            dsp->setParameterFloat(FMOD_DSP_TREMOLO_SKEW, 0.8f); // ？？？
            channel->addDSP(2, dsp); // 第三个音轨
            break;
        case com_walker_demo_fmod_Voice_MODE_KONGLING: // 空灵  学校广播
            content_ = "空灵 播放完毕";

// 回音 ECHO
            system->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
            dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY, 200); // 回音 延时    to 5000.  Default = 500.
            dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK, 10); // 回音 衰减度 Default = 50   0 完全衰减了
            channel->addDSP(0, dsp);
            break;

    }

// 等待播放完毕 再回收
    bool isPlayer = true; // 你用不是一级指针  我用一级指针接收你，可以修改给你
    while (isPlayer) {
        channel->isPlaying(&isPlayer); // 如果真的播放完成了，音轨是知道的，内部会修改isPlayer=false
        usleep(1000 * 1000); // 每个一秒
    }

// 时时刻刻记得回收
    sound->release();
    system->close();
    system->release();
    env->ReleaseStringUTFChars(path, path_);

// 告知Java播放完毕
    jclass mainCls = env->GetObjectClass(thiz);
    jmethodID endMethod = env->GetMethodID(mainCls, "playerEnd", "(Ljava/lang/String;)V");
    jstring value = env->NewStringUTF(content_);
    env->CallVoidMethod(thiz, endMethod, value);
}
