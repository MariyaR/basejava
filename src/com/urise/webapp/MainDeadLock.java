package com.urise.webapp;

public class MainDeadLock {
    private static final Object LOCK_1 = new Object();
    private static final Object LOCK_2 = new Object();

    public static void main(String[] args) {
        DeadLocker locker = new DeadLocker();

        Thread thread1 = new Thread(() -> locker.doSmth(LOCK_1, LOCK_2));
        Thread thread2 = new Thread(() -> locker.doSmth(LOCK_2, LOCK_1));
        thread1.start();
        thread2.start();
    }

    public static class DeadLocker {
        void doSmth(Object Lock1, Object Lock2) {
            synchronized (Lock1) {
                System.out.println("doSmth with " + Lock1);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (Lock2) {
                    System.out.println("doSmth with " + Lock2);
                }
            }
        }

//        synchronized void doSmth2() {
//            synchronized (Lock2) {
//                System.out.println("doSmth2 with Lock2");
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                synchronized (Lock1) {
//                    System.out.println("doSmth2 with Lock1");
//                }
//            }
//        }
    }
}
