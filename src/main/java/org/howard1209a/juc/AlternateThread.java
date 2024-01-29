package org.howard1209a.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class AlternateThread {
    private static volatile int state;
    private static Object lock = new Object();
    private static ReentrantLock rlock = new ReentrantLock();
    private static Condition condition = rlock.newCondition();
    private static volatile Thread t1;
    private static volatile Thread t2;
    private static volatile Thread t3;

    public static void main(String[] args) {
        method3();
    }

    public static void method1() {
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (lock) {
                        while (state == 1) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        System.out.println("threadA");
                        state = 1;
                        lock.notify();
                    }
                }
            }
        });
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (lock) {
                        while (state == 0) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        System.out.println("threadB");
                        state = 0;
                        lock.notify();
                    }
                }
            }
        });
        threadA.start();
        threadB.start();
    }

    private static void method2() {
        Thread threadA = new Thread(() -> {
            while (true) {
                rlock.lock();
                try {
                    while (state != 0) {
                        try {
                            condition.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("threadA");
                    state = 1;
                    condition.signal();
                } finally {
                    rlock.unlock();
                }
            }
        });
        Thread threadB = new Thread(() -> {
            while (true) {
                rlock.lock();
                try {
                    while (state != 1) {
                        try {
                            condition.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("threadB");
                    state = 0;
                    condition.signal();
                } finally {
                    rlock.unlock();
                }
            }
        });
        threadA.start();
        threadB.start();
    }

    private static void method3() {
        t1 = new Thread(() -> {
            while (true) {
                LockSupport.park();
                System.out.println("threadA");
                LockSupport.unpark(t2);
            }
        });
        t2 = new Thread(() -> {
            while (true) {
                LockSupport.park();
                System.out.println("threadB");
                LockSupport.unpark(t3);
            }
        });
        t3 = new Thread(() -> {
            while (true) {
                LockSupport.park();
                System.out.println("threadC");
                LockSupport.unpark(t1);
            }
        });
        t1.start();
        t2.start();
        t3.start();
        LockSupport.unpark(t1);
    }
}
