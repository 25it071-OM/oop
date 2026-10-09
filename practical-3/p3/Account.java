import java.util.Objects;

public class Account {
    private int accountNumber;
    private String ownerName;
    private double balance;

    public Account(int accountNumber, String ownerName, double balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    // Getter
    public int getAccountNumber() {
        return accountNumber;
    }

    // 1. toString()
    @Override
    public String toString() {
        return "Account Number: " + accountNumber +
               ", Owner Name: " + ownerName +
               ", Balance: " + balance;
    }

    // 2. equals()
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Account))
            return false;

        Account a = (Account) o;

        return this.accountNumber == a.accountNumber;
    }

    // 2. hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}