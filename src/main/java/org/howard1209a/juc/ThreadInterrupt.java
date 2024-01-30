package org.howard1209a.juc;

/**
 * 如何优雅地在一个线程打断另一个线程，使得被打断线程可以在执行一些finally代码后退出。
 * thread线程循环调用Thread.sleep(50)，这样可以少吃cpu。主线程和thread线程不一定运行在同一个cpu上，但由于顺序一致性我们可以看做运行在一个cpu上，因此主线程在thread.interrupt()的时候，thread线程可能是就绪/阻塞状态。如果是就绪状态，则interrupt()只会置打断标志位，不会影响线程的正常运行，又因为打断标志位本身保证了原子性，因此下一次的Thread.currentThread().isInterrupted()就会退出。如果是阻塞状态，则interrupt()不会置打断标志位，只会将thread线程转为就绪态，thread线程被调度运行后会抛出InterruptedException异常。
 */
public class ThreadInterrupt {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("finally...");
                    break;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.start();
        System.out.println("do something");
        thread.interrupt();
    }
}
