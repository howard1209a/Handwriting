package org.howard1209a.juc;

import lombok.Data;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicInt {
    private volatile int value;
    private static final Unsafe unsafe;
    private static final long offset;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
            offset = unsafe.objectFieldOffset(AtomicInt.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public AtomicInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean compareAndSet(int expect, int value) {
        return unsafe.compareAndSwapInt(this, offset, expect, value);
    }

    public void decrement(int num) {
        while (true) {
            int value = getValue();
            if (compareAndSet(value, value - num)) {
                break;
            }
        }
    }
}
