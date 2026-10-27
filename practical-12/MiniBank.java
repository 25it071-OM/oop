
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class Account implements Comparable<Account> {
    private final String accountNumber;
    private String ownerName;
    private long balance;

    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public void deposit(long amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    @Override
    public int compareTo(Account other) {
        return this.accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | Balance: " + balance;
    }
}

class AccountStore {
    private final HashMap<String, Account> accounts = new HashMap<>();
    private final ArrayList<String> transactionHistory = new ArrayList<>();

    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
        transactionHistory.add("Added account: " + account.getAccountNumber());
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public ArrayList<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> list = getAll();

        list.sort(Comparator.comparingLong(Account::getBalance)
                .reversed()
                .thenComparing(Account::getAccountNumber));

        return list;
    }
}

class TransactionProcessor {
    private final ConcurrentHashMap<String, Account> accounts =
            new ConcurrentHashMap<>();

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void deposit(String accountNumber, long amount) {
        Account account = accounts.get(accountNumber);

        if (account != null) {
            synchronized (account) {
                account.deposit(amount);
            }
        }
    }
}

public class MiniBank {
    public static void main(String[] args) {
        AccountStore store = new AccountStore();

        Account a1 = new Account("AC0001", "Om", 5000);
        Account a2 = new Account("AC0002", "Raj", 8000);
        Account a3 = new Account("AC0003", "Amit", 3000);
        Account a4 = new Account("AC0004", "Neha", 10000);

        store.add(a1);
        store.add(a2);
        store.add(a3);
        store.add(a4);

        System.out.println("Accounts Sorted by Balance:");
        for (Account account : store.sortedByBalance()) {
            System.out.println(account);
        }

        System.out.println("\nSearch Account AC0002:");
        System.out.println(store.get("AC0002"));

        System.out.println("\nTransaction History:");
        for (String transaction : store.getTransactionHistory()) {
            System.out.println(transaction);
        }

        TransactionProcessor processor = new TransactionProcessor();

        for (Account account : store.getAll()) {
            processor.addAccount(account);
        }

        processor.deposit("AC0001", 1000);

        System.out.println("\nAfter Deposit:");
        System.out.println(processor.getAccount("AC0001"));
    }
}
