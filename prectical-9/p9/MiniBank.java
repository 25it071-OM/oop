class Account {
    private long balance;

    public Account(long balance) {
        this.balance = balance;
    }

    public synchronized void deposit(long amount) {
        long oldBalance = balance;
        balance = oldBalance + amount;
    }

    public synchronized void withdraw(long amount) {
        long oldBalance = balance;
        if (oldBalance >= amount) {
            balance = oldBalance - amount;
        }
    }

    public long getBalance() {
        return balance;
    }
}

class AccountWorker implements Runnable {
    private Account account;
    private int times;
    private long amount;

    public AccountWorker(Account account, int times, long amount) {
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " started");

        for (int i = 0; i < times; i++) {
            account.deposit(amount);
        }

        System.out.println(Thread.currentThread().getName() + " finished");
    }
}

public class MiniBank {
    public static void main(String[] args) {
        Account account = new Account(0);

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            AccountWorker worker = new AccountWorker(account, 1000, 10);
            threads[i] = new Thread(worker, "Worker-" + (i + 1));
            threads[i].start();
        }

        for (int i = 0; i < 10; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }

        System.out.println("Final Balance = " + account.getBalance());
    }
}