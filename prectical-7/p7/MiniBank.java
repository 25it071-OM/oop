import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Id {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Positive {
    String message() default "must be > 0";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

@FunctionalInterface
interface WithdrawRule {
    boolean allow(Account account, long amount);
}

abstract class Account {

    @Id
    protected String accountNumber;

    @MaxLength(20)
    protected String ownerName;

    @Positive
    protected long balance;

    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    public long getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return accountNumber + " " + ownerName + " " + balance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Account))
            return false;

        Account other = (Account) obj;
        return Objects.equals(accountNumber, other.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accountNumber);
    }
}

class SavingsAccount extends Account {

    private long minBalance;

    public SavingsAccount(String accountNumber, String ownerName,
                          long balance, long minBalance) {
        super(accountNumber, ownerName, balance);
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return amount > 0 && balance - amount >= minBalance;
    }
}

class AnnotationValidator {

    public static String[] validate(Object obj) {
        List<String> errors = new ArrayList<>();

        Class<?> currentClass = obj.getClass();

        while (currentClass != null && currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                try {
                    Object value = field.get(obj);

                    if (field.isAnnotationPresent(Positive.class)) {
                        if (value instanceof Number
                                && ((Number) value).doubleValue() <= 0) {
                            Positive annotation =
                                    field.getAnnotation(Positive.class);

                            errors.add(field.getName() + " "
                                    + annotation.message());
                        }
                    }

                    if (field.isAnnotationPresent(MaxLength.class)) {
                        if (value instanceof String) {
                            MaxLength annotation =
                                    field.getAnnotation(MaxLength.class);

                            if (((String) value).length()
                                    > annotation.value()) {
                                errors.add(field.getName()
                                        + " length must be <= "
                                        + annotation.value());
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    errors.add("Cannot access " + field.getName());
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}

public class MiniBank {

    public static void main(String[] args) {

        Account account = new SavingsAccount(
                "AC0001", "Om Patel", -500, 1000);

        System.out.println("Account Details:");
        System.out.println(account);

        System.out.println("Interest Rate: "
                + account.interestRate() + "%");

        System.out.println("Can Withdraw: "
                + account.canWithdraw(100));

        WithdrawRule rule = (acc, amount) ->
                amount > 0 && acc.canWithdraw(amount);

        System.out.println("Withdraw Rule: "
                + rule.allow(account, 100));

        System.out.println("Validation Errors:");

        String[] errors = AnnotationValidator.validate(account);

        if (errors.length == 0) {
            System.out.println("Validation Passed");
        } else {
            for (String error : errors) {
                System.out.println(error);
            }
        }
    }
}