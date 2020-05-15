package com.walker.study.thread;

import android.util.Log;

public class Product {
    private static final String TAG = "Product";
    private String name;
    private int id;
    private boolean flag;

    public synchronized void put(String name) {
        if (!flag) {
            id++;
            this.name = name + " 商品编码：" + id;
            Log.i(TAG, Thread.currentThread().getName() + "生产者，生产了：" + this.name);

            flag = true;
            notify();
            try {
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void out() {
        if (flag) {
            Log.i(TAG, Thread.currentThread().getName() + "消费者，消费了：" + this.name);
            flag = false;
            notify();
            try {
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
