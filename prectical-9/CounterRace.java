class Counter {

    int count = 0;

    // Without synchronization
    void increment() {
        count++;
    }

    // For synchronized version:
    // synchronized void increment() {
    //     count++;
    // }
}

class CounterThread extends Thread {

    Counter counter;
    int times;

    CounterThread(Counter counter, int times) {
        this.counter = counter;
        this.times = times;
    }

    public void run() {
        for (int i = 0; i < times; i++) {
            counter.increment();
        }
    }
}

public class CounterRace {

    public static void main(String[] args)
            throws InterruptedException {

        Counter counter = new Counter();

        int threads = 10;
        int times = 100000;

        CounterThread[] t = new CounterThread[threads];

        for (int i = 0; i < threads; i++) {
            t[i] = new CounterThread(counter, times);
            t[i].start();
        }

        // Wait for all threads
        for (int i = 0; i < threads; i++) {
            t[i].join();
        }

        int expected = threads * times;

        System.out.println("Expected = " + expected);
        System.out.println("Actual   = " + counter.count);
    }
}