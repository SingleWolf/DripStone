package com.walker.asm;

import java.util.ArrayList;

public class AopTraceExt {
    private boolean enable;
    private ArrayList<String> tracePrefix = new ArrayList<>();

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public ArrayList<String> getTracePrefix() {
        return tracePrefix;
    }

    public void setTracePrefix(ArrayList<String> list) {
        this.tracePrefix.addAll(list);
    }
}
