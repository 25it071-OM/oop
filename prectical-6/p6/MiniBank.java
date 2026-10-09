import static java.lang.Math.round;

interface Transactable {
    void deposit(long amount);
    boolean withdraw(long amount);
}

interface InterestBearing {
    double interestRate();
    long getBalance();

    default double yearlyInterest() {
        return getBalance() * interestRate() / 100;
    }
}

@FunctionalInterface
interface WithdrawRule {
    boolean allow(Account account, long amount);
}

interface Premium {
}

abstract class Account implements Transactable, InterestBearing {
    protected String accountNumber;
    protected String ownerName;
    protected long balance;

    Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    abstract boolean canWithdraw(long amount);

    public void deposit(long amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(long amount) {
        if (amount > 0 && canWithdraw(amount)) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public long getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }
}

class SavingsAccount extends Account {
    private long minBalance;

    SavingsAccount(String accountNumber, String ownerName,
                   long balance, long minBalance) {
        super(accountNumber, ownerName, balance);
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    boolean canWithdraw(long amount) {
        return balance - amount >= minBalance;
    }
}

class CurrentAccount extends Account {
    private long overdraftLimit;

    CurrentAccount(String accountNumber, String ownerName,
                   long balance, long overdraftLimit) {
        super(accountNumber, ownerName, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double interestRate() {
        return 0.0;
    }

    @Override
    boolean canWithdraw(long amount) {
        return balance - amount >= -overdraftLimit;
    }
}

class FixedDepositAccount extends Account {

    FixedDepositAccount(String accountNumber, String ownerName,
                        long balance) {
        super(accountNumber, ownerName, balance);
    }

    @Override
    public double interestRate() {
        return 7.0;
    }

    @Override
    boolean canWithdraw(long amount) {
        return false;
    }
}

public class MiniBank {
    public static void main(String[] args) {

        Account[] accounts = {
            new SavingsAccount("SA001", "Om", 10000, 3000),
            new CurrentAccount("CA001", "Rahul", 5000, 3000),
            new FixedDepositAccount("FD001", "Jay", 20000)
        };

        for (Account account : accounts) {
            System.out.println(
                account.getAccountNumber()
                + " Interest: "
                + account.interestRate() + "%"
            );
        }

        WithdrawRule rule1 = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return account.canWithdraw(amount);
            }
        };

        System.out.println(
            "Anonymous: " + rule1.allow(accounts[0], 5000)
        );

        WithdrawRule rule2 =
            (account, amount) -> account.canWithdraw(amount);

        System.out.println(
            "Lambda: " + rule2.allow(accounts[1], 7000)
        );

        if (accounts[0] instanceof SavingsAccount savings) {
            System.out.println(
                "Savings: " + savings.getAccountNumber()
            );
        }

        long value = round(45.6);
        System.out.println("Rounded: " + value);
    }
}