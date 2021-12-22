package com.walker.core.efficiency.taskflow.exception;

public class UnSupportOperateException extends IllegalStateException {
    public UnSupportOperateException() {
        super("You can not operate a disposed task flow");
    }
}
