package org.howard1209a.juc;

/**
 * 线程安全的多线程数据容器
 */
public class GuardedObject {
    private volatile Object data;

    /**
     * 写入数据
     */
    public synchronized void set(Object data) {
        this.data = data;
        this.notifyAll();
    }

    /**
     * 阻塞式地获取数据，超时返回null，单位ms。即使出现了虚假通知（data为null时wait提前返回）也不会出现问题
     */
    public Object get(long timeout) {
        synchronized (this) {
            timeout += System.currentTimeMillis();
            while (data == null) {
                long now = System.currentTimeMillis();
                if (now >= timeout) {
                    return null;
                }
                try {
                    wait(timeout - now);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return data;
        }
    }

    /**
     * 阻塞式地获取数据
     */
    public Object get() {
        synchronized (this) {
            while (data == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return data;
        }
    }
}
