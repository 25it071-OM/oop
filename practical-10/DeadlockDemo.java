public class DeadlockDemo {
    static final Object lock1 = new Object();
    static final Object lock2 = new Object();

    static void deadlock() {
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Thread 1 locked lock1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                synchronized (lock2) {
                    System.out.println("Thread 1 locked lock2");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("Thread 2 locked lock2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                synchronized (lock1) {
                    System.out.println("Thread 2 locked lock1");
                }
            }
        });

        t1.start();
        t2.start();
    }

    static void fixedOrder() {
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                synchronized (lock2) {
                    System.out.println("Thread 1 completed safely");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lock1) {
                synchronized (lock2) {
                    System.out.println("Thread 2 completed safely");
                }
            }
        });
        t1.start();
        t2.start();
    }

    public static void main(String[] args) {
        System.out.println("1. Deadlock demonstration");
        deadlock();

        System.out.println("2. Fixed-order solution");
        fixedOrder();
    }
}