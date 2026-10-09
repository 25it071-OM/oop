```java
class CurrentAccount extends Account {

    private long overdraftLimit;

    public CurrentAccount(String accountNumber, String ownerName,
                          long balance, long overdraftLimit) {

        super(accountNumber, ownerName, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    double interestRate() {
        return 0.0;
    }

    @Override
    boolean canWithdraw(long amount) {
        return balance - amount >= -overdraftLimit;
    }
}
```
