import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Account {
    private final String accountNumber;
    private long balance;

    public Account(String accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public synchronized void deposit(long amount) {
        balance += amount;
    }

    public synchronized void withdraw(long amount) {
        if (amount <= balance) {
            balance -= amount;
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public synchronized long getBalance() {
        return balance;
    }
}

class TransactionProcessor {
    private final ExecutorService executor;

    public TransactionProcessor() {
        executor = Executors.newFixedThreadPool(4);
    }

    public void submit(Runnable task) {
        executor.execute(task);
    }

    public void stop() {
        executor.shutdown();

        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class TransactionTask implements Runnable {
    private final Account account;
    private final boolean deposit;
    private final long amount;

    public TransactionTask(Account account, boolean deposit, long amount) {
        this.account = account;
        this.deposit = deposit;
        this.amount = amount;
    }

    @Override
    public void run() {
        if (deposit) {
            account.deposit(amount);
            System.out.println(Thread.currentThread().getName()
                    + " deposited " + amount);
        } else {
            account.withdraw(amount);
            System.out.println(Thread.currentThread().getName()
                    + " withdrew " + amount);
        }
    }
}

class TransactionBuffer {
    private final int[] buffer;
    private int count = 0;

    public TransactionBuffer(int size) {
        buffer = new int[size];
    }

    public synchronized void add(int value) throws InterruptedException {
        while (count == buffer.length) {
            wait();
        }

        buffer[count++] = value;
        System.out.println("Produced: " + value);

        notify();
    }

    public synchronized int remove() throws InterruptedException {
        while (count == 0) {
            wait();
        }

        int value = buffer[--count];
        System.out.println("Consumed: " + value);

        notify();
        return value;
    }
}

class SafeTransfer {
    public static void transfer(Account a, Account b, long amount) {
        Account first;
        Account second;

        if (a.getAccountNumber().compareTo(b.getAccountNumber()) < 0) {
            first = a;
            second = b;
        } else {
            first = b;
            second = a;
        }

        synchronized (first) {
            synchronized (second) {
                a.withdraw(amount);
                b.deposit(amount);

                System.out.println(
                        Thread.currentThread().getName()
                        + " transferred " + amount
                );
            }
        }
    }
}

public class MiniBank {
    public static void main(String[] args) {

        Account a = new Account("AC001", 10000);
        Account b = new Account("AC002", 10000);

        System.out.println("=== Thread Pool ===");

        TransactionProcessor processor = new TransactionProcessor();

        for (int i = 1; i <= 5; i++) {
            processor.submit(new TransactionTask(a, true, 1000));
        }

        for (int i = 1; i <= 3; i++) {
            processor.submit(new TransactionTask(a, false, 500));
        }

        processor.stop();

        System.out.println("Final Balance: " + a.getBalance());

        System.out.println("\n=== Producer Consumer ===");

        TransactionBuffer buffer = new TransactionBuffer(3);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    buffer.add(i * 100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    buffer.remove();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== Safe Transfer ===");

        Thread t1 = new Thread(() -> {
            SafeTransfer.transfer(a, b, 1000);
        }, "Transfer-A-B");

        Thread t2 = new Thread(() -> {
            SafeTransfer.transfer(b, a, 500);
        }, "Transfer-B-A");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Account A: " + a.getBalance());
        System.out.println("Account B: " + b.getBalance());
    }
}