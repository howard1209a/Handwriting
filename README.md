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

