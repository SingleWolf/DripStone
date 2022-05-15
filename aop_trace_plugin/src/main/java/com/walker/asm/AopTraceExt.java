package com.walker.asm;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;

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
        for (String prefix : list) {
            tracePrefix.add(prefix.replaceAll(Matcher.quoteReplacement(File.separator), "/"));
        }
    }
}
