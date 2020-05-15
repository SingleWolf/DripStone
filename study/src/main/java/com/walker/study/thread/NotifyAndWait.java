package com.walker.study.thread;

/**
 * @Author Walker
 * @Date 2020-05-15 09:35
 * @Summary 利用线程的notify和wait实现边生产边消费逻辑
 */
public class NotifyAndWait {
    private static NotifyAndWait sInstance;

    private NotifyAndWait() {
    }

    public static NotifyAndWait getInstance() {
        if (sInstance == null) {
            synchronized (NotifyAndWait.class) {
                if (sInstance == null) {
                    sInstance = new NotifyAndWait();
                }
            }
        }
        return sInstance;
    }

    class Produce implements Runnable {

        private Product product;

        public Produce(Product p) {
            product = p;
        }

        @Override
        public void run() {
            if (product != null) {
                for (int i = 0; i < 20; i++) {
                    product.put("旺仔小馒头");
                }
            }
        }
    }

    class Consume implements Runnable {

        private Product product;

        public Consume(Product p) {
            product = p;
        }

        @Override
        public void run() {
            if (product != null) {
                for (int i = 0; i < 20; i++) {
                    product.out();
                }
            }
        }
    }

    public void test() {
        Product product = new Product();
        //生产者
        Produce produce = new Produce(product);
        //消费者
        Consume consume = new Consume(product);

        new Thread(produce).start();
        new Thread(consume).start();
    }
}
