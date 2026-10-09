class BankException extends Exception {

    public BankException(String message) {
        super(message);
    }
}

class InsufficientFundsException extends BankException {

    private long shortfall;

    public InsufficientFundsException(String message, long shortfall) {
        super(message);
        this.shortfall = shortfall;
    }

    public long getShortfall() {
        return shortfall;
    }
}

class AccountNotFoundException extends BankException {

    public AccountNotFoundException(String message) {
        super(message);
    }
}

class InvalidAmountException extends BankException {

    public InvalidAmountException(String message) {
        super(message);
    }
}

class Account {

    private String accountNumber;
    private String ownerName;
    private long balance;

    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public void deposit(long amount)
            throws InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be greater than 0"
            );
        }

        balance += amount;
    }

    public void withdraw(long amount)
            throws InsufficientFundsException,
                   InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be greater than 0"
            );
        }

        if (amount > balance) {
            long shortfall = amount - balance;

            throw new InsufficientFundsException(
                    "Insufficient funds",
                    shortfall
            );
        }

        balance -= amount;
    }

    public void transfer(Account to, long amount)
            throws BankException {

        try {
            if (to == null) {
                throw new AccountNotFoundException(
                        "Destination account not found"
                );
            }

            withdraw(amount);
            to.deposit(amount);

        } catch (InvalidAmountException e) {

            throw e;

        } catch (InsufficientFundsException e) {

            throw e;

        } finally {

            System.out.println("Transfer operation completed");
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public long getBalance() {
        return balance;
    }
}

class BankResource implements AutoCloseable {

    public void open() {
        System.out.println("Resource opened");
    }

    @Override
    public void close() {
        System.out.println("Resource closed");
    }
}

public class MiniBank {

    public static void main(String[] args) {

        Account a1 =
                new Account("AC001", "Om", 5000);

        Account a2 =
                new Account("AC002", "Rahul", 2000);

        try {

            a1.deposit(1000);
            System.out.println(
                    "Deposit successful"
            );

            a1.withdraw(2000);
            System.out.println(
                    "Withdrawal successful"
            );

            a1.transfer(a2, 1000);
            System.out.println(
                    "Transfer successful"
            );

        } catch (InsufficientFundsException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );

            System.out.println(
                    "Shortfall: " + e.getShortfall()
            );

        } catch (InvalidAmountException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );

        } catch (BankException e) {

            System.out.println(
                    "Bank Error: " + e.getMessage()
            );

        } finally {

            System.out.println(
                    "Transaction process finished"
            );
        }

        try (BankResource resource = new BankResource()) {

            resource.open();
            System.out.println(
                    "Using bank resource"
            );

        } catch (Exception e) {

            System.out.println(
                    "Resource Error: " + e.getMessage()
            );
        }
    }
}