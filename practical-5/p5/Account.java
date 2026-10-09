```java
abstract class Account {
    protected final String accountNumber;
    protected final String ownerName;
    protected long balance;
    protected boolean active;

    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }

    // Abstract methods
    abstract double interestRate();

    abstract boolean canWithdraw(long amount);

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
```
