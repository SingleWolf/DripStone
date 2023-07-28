cmake_minimum_required(VERSION 3.4.1)

include_directories(breakpad/src breakpad/src/common/android/include)

include_directories(airbag)
#so库的输出路径
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI})

# libbugly.so
add_library(bugly SHARED bugly.cpp)

add_subdirectory(breakpad)

add_subdirectory(airbag)

# 链接ndk中的log库
target_link_libraries(bugly  breakpad nativeAirbag log)