cmake_minimum_required(VERSION 3.4.1)

#对应android.mk中的 LOCAL_C_INCLUDES
include_directories(src src/common/android/include)
#开启arm汇编支持，因为在源码中有 .S文件（汇编源码）
enable_language(ASM)
#生成 libbreakpad.a 并指定源码，对应android.mk中 LOCAL_SRC_FILES+LOCAL_MODULE
add_library(breakpad STATIC
        src/client/linux/crash_generation/crash_generation_client.cc
        src/client/linux/dump_writer_common/thread_info.cc
        src/client/linux/dump_writer_common/ucontext_reader.cc
        src/client/linux/handler/exception_handler.cc
        src/client/linux/handler/minidump_descriptor.cc
        src/client/linux/log/log.cc
        src/client/linux/microdump_writer/microdump_writer.cc
        src/client/linux/minidump_writer/linux_dumper.cc
        src/client/linux/minidump_writer/linux_ptrace_dumper.cc
        src/client/linux/minidump_writer/minidump_writer.cc
        src/client/minidump_file_writer.cc
        src/common/convert_UTF.cc
        src/common/md5.cc
        src/common/string_conversion.cc
        src/common/linux/breakpad_getcontext.S
        src/common/linux/elfutils.cc
        src/common/linux/file_id.cc
        src/common/linux/guid_creator.cc
        src/common/linux/linux_libc_support.cc
        src/common/linux/memory_mapped_file.cc
        src/common/linux/safe_readlink.cc)

#链接 log库，对应android.mk中 LOCAL_EXPORT_LDLIBS
target_link_libraries(breakpad log)