package org.howard1209a.juc;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 哲学家就餐问题，run1会死锁，run2非阻塞式获取锁解决死锁，run3锁超时解决死锁
 * */
public class DeadlockResolve {
    public static void main(String[] args) {
        ReentrantLock[] reentrantLocks = new ReentrantLock[5];
        Philosopher[] philosophers = new Philosopher[5];
        for (int i = 0; i < 5; i++) {
            reentrantLocks[i] = new ReentrantLock();
        }
        for (int i = 0; i < 5; i++) {
            philosophers[i] = new Philosopher("Philosopher" + i, reentrantLocks[i], reentrantLocks[(i + 1) % 5]);
        }
        Arrays.stream(philosophers).forEach((philosopher) -> {
            philosopher.start();
        });
    }

    private static class Philosopher extends Thread {
        private String name;
        private ReentrantLock leftLock;
        private ReentrantLock rightLock;

        public Philosopher(String name, ReentrantLock leftLock, ReentrantLock rightLock) {
            this.name = name;
            this.leftLock = leftLock;
            this.rightLock = rightLock;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    run3();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void run1() {
            leftLock.lock();
            try {
                rightLock.lock();
                try {
                    doSomething();
                } finally {
                    rightLock.unlock();
                }
            } finally {
                leftLock.unlock();
            }
        }

        private void run2() {
            boolean result = leftLock.tryLock();
            if (result) {
                try {
                    result = rightLock.tryLock();
                    if (result) {
                        try {
                            doSomething();
                        } finally {
                            rightLock.unlock();
                        }
                    }
                } finally {
                    leftLock.unlock();
                }
            }
        }

        private void run3() throws InterruptedException {
            boolean result = leftLock.tryLock(100, TimeUnit.MILLISECONDS);
            if (result) {
                try {
                    result = rightLock.tryLock(100, TimeUnit.MILLISECONDS);
                    if (result) {
                        try {
                            doSomething();
                        } finally {
                            rightLock.unlock();
                        }
                    }
                } finally {
                    leftLock.unlock();
                }
            }
        }

        private void doSomething() {
            System.out.println(name + " doSomething");
        }
    }
}
