package com.urise.webapp;

public class MainDeadLock {

    public static void main(String[] args) {
        DeadLocker locker = new DeadLocker();

        Thread thread1 = new Thread(() -> locker.doSmth1());
        Thread thread2 = new Thread(() -> locker.doSmth2());
        thread1.start();
        thread2.start();
    }

    public static class DeadLocker {
        Object Lock1 = new Object();
        Object Lock2 = new Object();

        void doSmth1() {
            synchronized (Lock1) {
                System.out.println("doSmth1 with Lock1");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (Lock2) {
                    System.out.println("doSmth1 with Lock2");
                }
            }
        }

        synchronized void doSmth2() {
            synchronized (Lock2) {
                System.out.println("doSmth2 with Lock2");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (Lock1) {
                    System.out.println("doSmth2 with Lock1");
                }
            }
        }
    }
}
