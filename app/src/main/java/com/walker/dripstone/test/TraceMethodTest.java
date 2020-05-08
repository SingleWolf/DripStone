package com.walker.dripstone.test;

import com.walker.demo.trace.MethodEventManager;
import com.walker.demo.trace.TimeObserver;
import com.walker.demo.trace.TraceMethod;

public class TraceMethodTest {

    public TraceMethodTest() {
        MethodEventManager.getInstance().registerMethodObserver("testAnd", new TimeObserver());
    }

    @TraceMethod(tag = "testAnd")
    private void test() {
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void tryTest() {
        test();
    }
}
