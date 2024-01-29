package org.howard1209a.juc;

public class DoubleCheckedLocking {
    private static volatile DoubleCheckedLocking dcl; // 多线程读写，一定要加volatile

    private DoubleCheckedLocking() {
    }

    public static DoubleCheckedLocking getInstance() {
        if (dcl != null) {
            return dcl;
        }
        synchronized (DoubleCheckedLocking.class) {
            if (dcl != null) {
                return dcl;
            }
            dcl = new DoubleCheckedLocking();
            return dcl;
        }
    }
}
