package com.depeng.concurrent;

import java.util.concurrent.CountDownLatch;

public class Driver {
    static int anInt = 8;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(anInt);

        for (int i = 0; i < anInt; i++) {
            Worker target = new Worker(startSignal, doneSignal);
            new Thread(target).start();
        }

        System.out.println("[I am in main thread]");
        startSignal.countDown();
        System.out.println("[I am in main thread] start");
        doneSignal.await();
        System.out.println("[I am in main thread] finish");
    }

    static class Worker implements Runnable {
        private final CountDownLatch startSignal;
        private final CountDownLatch done;

        Worker(CountDownLatch startSignal, CountDownLatch done) {
            this.startSignal = startSignal;
            this.done = done;
        }

        @Override
        public void run() {
            try {
                startSignal.await(); // every worker thread have the reference of start latch and wait by
                System.out.println(Thread.currentThread().getId());
                done.countDown();    // every worker thread have the reference of done latch and count down
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
