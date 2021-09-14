package com.walker.core.log;

public interface ILogger {
    void d(String tag, String message, boolean isSave);

    void i(String tag, String message, boolean isSave);

    void w(String tag, String message, boolean isSave);

    void e(String tag, String message, boolean isSave);

    void start();

    void close();

    void flush();
}
