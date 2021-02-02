package com.walker.optimize.group.trace;

public class TraceTest {
    public TraceTest() {
        MethodEventManager.getInstance().registerMethodObserver("trace_test", new TimeObserver());
    }

    @TraceMethod(tag = "trace_test")
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
