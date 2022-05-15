package com.walker.study.hotfix;


import dalvik.system.PathClassLoader;

public class HotfixClassLoader extends PathClassLoader {
    public HotfixClassLoader(String dexPath, String librarySearchPath, ClassLoader parent) {
        super(dexPath, librarySearchPath, parent);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }
}
