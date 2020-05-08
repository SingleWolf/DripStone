package com.walker.buildsrc.trace;

public class TraceMethodExt {
    private boolean enable;
    private String tracePrefix;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getTracePrefix() {
        return tracePrefix;
    }

    public void setTracePrefix(String tracePrefix) {
        this.tracePrefix = tracePrefix;
    }
}
