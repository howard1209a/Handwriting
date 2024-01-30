package org.howard1209a.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class SequentialThread {
    private static volatile boolean done;
    private static Object lock = new Object();
    private static ReentrantLock rlock = new ReentrantLock();
    private static Condition condition = rlock.newCondition();

    public static void main(String[] args) {
        method4();
    }

    private static void method1() {
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println("threadA");
                    done = true;
                    lock.notify();
                }
            }
        });
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    while (!done) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("threadB");
                }
            }
        });
        threadA.start();
        threadB.start();
    }

    private static void method2() {
        Thread threadA = new Thread(() -> {
            rlock.lock();
            try {
                System.out.println("threadA");
                done = true;
                condition.signal();
            } finally {
                rlock.unlock();
            }
        });
        Thread threadB = new Thread(() -> {
            rlock.lock();
            try {
                while (!done) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("threadB");
            } finally {
                rlock.unlock();
            }
        });
        threadA.start();
        threadB.start();
    }

    private static void method3() {
        Thread threadA = new Thread(() -> {
            System.out.println("threadA");
        });
        Thread threadB = new Thread(() -> {
            try {
                threadA.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("threadB");
        });
        threadA.start();
        threadB.start();
    }

    private static void method4() {
        Thread threadB = new Thread(() -> {
            LockSupport.park();
            System.out.println("threadB");
        });
        Thread threadA = new Thread(() -> {
            System.out.println("threadA");
            LockSupport.unpark(threadB);
        });
        threadA.start();
        threadB.start();
    }
}
