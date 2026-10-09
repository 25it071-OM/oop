class FixedDepositAccount extends Account {

    public FixedDepositAccount(String accountNumber, String ownerName,
                               long balance) {

        super(accountNumber, ownerName, balance);
    }

    @Override
    double interestRate() {
        return 7.0;
    }

    @Override
    boolean canWithdraw(long amount) {
        return false;
    }
}
```
