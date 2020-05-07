package com.walker.buildsrc.hotfix;

public class HotfixExt {
    private boolean onDebug;
    private String outputPath;
    private String applicationName;

    public boolean isOnDebug() {
        return onDebug;
    }

    public void setOnDebug(boolean onDebug) {
        this.onDebug = onDebug;
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
        this.applicationName = applicationName;
    }
}
