```java
class SavingsAccount extends Account {

    private long minBalance;

    public SavingsAccount(String accountNumber, String ownerName,
                          long balance, long minBalance) {

        super(accountNumber, ownerName, balance);
        this.minBalance = minBalance;
    }

    @Override
    double interestRate() {
        return 4.0;
    }

    @Override
    boolean canWithdraw(long amount) {
        return balance - amount >= minBalance;
    }
}
```
