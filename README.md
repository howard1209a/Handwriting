# 一些手写的实现

## juc系列

### GuardedObject

实现一个多线程数据容器，要求实现阻塞式数据获取接口，阻塞式但可打断可超时的数据获取接口，数据设置接口

### MessageQueue

固定容量阻塞式消息队列，要求实现阻塞式进队，阻塞式出队

### ThreadInterrupt

打断Thread.sleep的一个例子

### DeadlockResolve

非阻塞式锁/锁超时解决哲学家就餐死锁问题

### SequentialThread

两个线程AB同时运行，A先打印，B再打印

- synchronized wait notify
- ReentrantLock await signal
- join
- park unpark

### AlternateThread

两个线程AB同时运行，AB交替打印

- synchronized wait notify
- ReentrantLock await signal
- park unpark

### DoubleCheckedLocking

懒惰加载的单例模式或唯一缓存重建一般都这么写，要注意volatile

### MultiThreadLock

多线程锁，提供多线程阻塞式获取锁和原子释放锁两个接口，支持一个线程获取的锁在另一个线程释放

### AtomicInt

利用Unsafe对象实现了原子int，提供了cas操作和以及无锁的线程安全减法

### ConnectionPool

连接池，支持多线程阻塞式借连接，归还连接，采用cas无锁实现

### ThreadPool

#### BlockingQueue

固定长度阻塞队列，提供阻塞式入队接口、超时退出的阻塞式入队接口、阻塞式出队接口

#### ThreadPool

提供execute接口，提交一个任务。如果当前线程数没有达到coreSize则创建一个线程并执行这个任务，如果线程数已满，则提交给任务队列，任务队列已满则阻塞。线程池中的线程会不断地从任务队列中取出任务并执行，如果线程太长时间没有执行任务则会结束。

### AQSLock

基于AQS实现的一个自定义阻塞式锁，实现了Lock接口规定的阻塞式获取锁、可打断阻塞式获取锁、非阻塞式获取锁、可打断可超时阻塞式获取锁、释放锁方法