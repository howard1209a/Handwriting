package org.howard1209a.juc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 固定容量阻塞式消息队列
 */
public class MessageQueue {
    private List<Message> messages; // 只涉及多线程读不需要标记成volatile的
    private int capacity; // 只涉及多线程读不需要标记成volatile的

    public MessageQueue(int capacity) {
        this.messages = new ArrayList<>();
        this.capacity = capacity;
    }

    /**
     * 阻塞式进队
     */
    public synchronized void add(Message message) {
        while (messages.size() == capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        notifyAll();
        messages.add(message);
    }

    /**
     * 阻塞式出队
     */
    public Message poll() {
        synchronized (this) {
            while (messages.size() == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            notifyAll();
            return messages.remove(0);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Message {
        private volatile int id;
        private volatile Object value;
    }
}
