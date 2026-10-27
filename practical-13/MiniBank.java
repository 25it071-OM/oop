
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Id {
}

class Account {
    @Id
    private String accountNumber;
    private String ownerName;
    private long balance;

    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | Balance: " + balance;
    }
}

class Repository<T> {
    private final HashMap<String, T> data = new HashMap<>();

    public T save(T entity) {
        try {
            Class<?> clazz = entity.getClass();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    Object value = field.get(entity);

                    if (value == null) {
                        throw new IllegalArgumentException("ID cannot be null");
                    }

                    String id = value.toString();
                    data.put(id, entity);
                    return entity;
                }
            }

            throw new IllegalArgumentException("@Id field not found");

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public T findById(String id) {
        return data.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(data.values());
    }

    public boolean delete(String id) {
        return data.remove(id) != null;
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        for (T item : items) {
            save(item);
        }
    }

    public static <N extends Number> double average(N[] values) {
        if (values.length == 0) {
            return 0.0;
        }

        double sum = 0;

        for (N value : values) {
            sum += value.doubleValue();
        }

        return sum / values.length;
    }
}

public class MiniBank {
    public static void main(String[] args) {
        Repository<Account> repository = new Repository<>();

        Account a1 = new Account("AC0001", "Om", 5000);
        Account a2 = new Account("AC0002", "Raj", 8000);
        Account a3 = new Account("AC0003", "Amit", 3000);

        repository.saveAll(a1, a2, a3);

        System.out.println("Account found by ID:");
        System.out.println(repository.findById("AC0002"));

        System.out.println("\nAll Accounts:");
        for (Account account : repository.findAll()) {
            System.out.println(account);
        }

        System.out.println("\nDelete AC0003: " +
                repository.delete("AC0003"));

        Integer[] numbers = {10, 20, 30};
        System.out.println("Average: " +
                Repository.average(numbers));
    }
}
