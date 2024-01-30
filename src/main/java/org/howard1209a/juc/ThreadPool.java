package org.howard1209a.juc;

import com.sun.corba.se.spi.orbutil.threadpool.Work;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool {
    private BlockingQueue<Runnable> taskQueue;
    private HashSet<Worker> workers;
    private final int coreSize;
    private final long timeout;
    private final TimeUnit timeUnit;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit) {
        this.taskQueue = new BlockingQueue<>(2);
        this.workers = new HashSet<>();
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public void execute(Runnable task) {
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                workers.add(worker);
                worker.start();
            } else {
                taskQueue.put(task);
            }
        }
    }


    private class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            while (task != null || (task = taskQueue.poll(timeout, timeUnit)) != null) {
                try {
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                task = null;
            }
            synchronized (workers) {
                workers.remove(this);
            }
        }
    }

    private static class BlockingQueue<T> {
        private Deque<T> queue = new ArrayDeque<>();
        private ReentrantLock lock = new ReentrantLock();
        private Condition emptyWaitSet = lock.newCondition();
        private Condition fullWaitSet = lock.newCondition();
        private final int capacity;

        public BlockingQueue(int capacity) {
            this.capacity = capacity;
        }

        // 超时退出的阻塞式入队
        public T poll(long timeout, TimeUnit timeUnit) {
            long nanos = timeUnit.toNanos(timeout);
            lock.lock();
            try {
                while (queue.isEmpty()) {
                    if (nanos <= 0) {
                        return null;
                    }
                    try {
                        nanos = emptyWaitSet.awaitNanos(nanos);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                fullWaitSet.signal();
                return queue.pollFirst();
            } finally {
                lock.unlock();
            }
        }

        // 阻塞式入队
        public T take() {
            lock.lock();
            try {
                while (queue.isEmpty()) {
                    try {
                        emptyWaitSet.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                fullWaitSet.signal();
                return queue.pollFirst();
            } finally {
                lock.unlock();
            }
        }

        // 阻塞式出队
        public void put(T t) {
            lock.lock();
            try {
                while (queue.size() == capacity) {
                    try {
                        fullWaitSet.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                emptyWaitSet.signal();
                queue.add(t);
            } finally {
                lock.unlock();
            }
        }

        public int size() {
            lock.lock();
            try {
                return queue.size();
            } finally {
                lock.unlock();
            }
        }
    }
}
