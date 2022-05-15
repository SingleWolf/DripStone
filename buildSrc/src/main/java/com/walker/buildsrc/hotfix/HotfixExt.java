package com.walker.buildsrc.hotfix;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class HotfixExt {
    private boolean onDebug;
    private boolean open;
    private String outputPath;
    private String applicationName;
    private ArrayList<String> fixPrefix = new ArrayList<>();

    public boolean isOnDebug() {
        return onDebug;
    }

    public void setOnDebug(boolean onDebug) {
        this.onDebug = onDebug;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName.replaceAll(Matcher.quoteReplacement(File.separator), "/");
    }

    public ArrayList<String> getFixPrefix() {
        return fixPrefix;
    }

    public void setFixPrefix(ArrayList<String> fixs) {
        for (String prefix : fixs) {
            fixPrefix.add(prefix.replaceAll(Matcher.quoteReplacement(File.separator), "/"));
        }
    }
}
