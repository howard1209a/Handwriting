package org.howard1209a.juc;

import java.util.concurrent.locks.Lock;

public class MultiThreadLock {
    private boolean locked;

    public void lock() {
        synchronized (this) {
            while (locked) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            locked = true;
        }
    }

    public void unlock() {
        synchronized (this) {
            locked = false;
            notifyAll();
        }
    }
}
