import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    String name;

    Customer(String name) {
        this.name = name;
    }
}

class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    String accountNumber;
    String ownerName;
    long balance;

    Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    void deposit(long amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    boolean withdraw(long amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName +
               " | Balance: " + balance;
    }
}

class StatePersister {
    public static void save(Account[] accounts, Path file)
            throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                Files.newOutputStream(file))) {
            out.writeObject(accounts);
        }
    }

    public static Account[] load(Path file)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(
                Files.newInputStream(file))) {
            return (Account[]) in.readObject();
        }
    }
}

class TransactionLog {
    public static void append(Path file, String line)
            throws IOException {
        Files.write(file,
                (line + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }
}

class ReportGenerator {
    public static void generate(Path logsFolder, Path reportFile)
            throws IOException {

        long totalDeposits = 0;
        long totalWithdrawals = 0;

        try (BufferedWriter writer = Files.newBufferedWriter(
                reportFile,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.write("MINIBANK END-OF-DAY REPORT");
            writer.newLine();
            writer.write("==========================");
            writer.newLine();

            try (DirectoryStream<Path> stream =
                    Files.newDirectoryStream(logsFolder)) {

                for (Path file : stream) {
                    if (!Files.isRegularFile(file)) {
                        continue;
                    }

                    BasicFileAttributes attrs =
                            Files.readAttributes(
                                file, BasicFileAttributes.class);

                    writer.newLine();
                    writer.write("File: " + file.getFileName());
                    writer.newLine();
                    writer.write("Size: " + attrs.size() + " bytes");
                    writer.newLine();
                    writer.write("Modified: " +
                            attrs.lastModifiedTime());
                    writer.newLine();

                    try (BufferedReader reader =
                            Files.newBufferedReader(file)) {
                        String line;

                        while ((line = reader.readLine()) != null) {
                            writer.write(line);
                            writer.newLine();

                            String[] parts = line.split("\\s+");

                            if (parts.length >= 2) {
                                try {
                                    long amount =
                                            Long.parseLong(parts[1]);

                                    if (parts[0].equalsIgnoreCase("DEPOSIT")) {
                                        totalDeposits += amount;
                                    } else if (parts[0].equalsIgnoreCase("WITHDRAW")) {
                                        totalWithdrawals += amount;
                                    }
                                } catch (NumberFormatException e) {
                                    // Ignore lines without a valid amount.
                                }
                            }
                        }
                    }
                }
            }

            writer.newLine();
            writer.write("TOTAL DEPOSITS: " + totalDeposits);
            writer.newLine();
            writer.write("TOTAL WITHDRAWALS: " + totalWithdrawals);
            writer.newLine();
        }
    }
}

public class MiniBank {
    public static void main(String[] args) throws Exception {
        Path dataFile = Paths.get("accounts.dat");
        Path logsFolder = Paths.get("logs");
        Path reportFile = Paths.get("report.txt");

        Files.createDirectories(logsFolder);

        Account[] accounts = {
            new Account("AC0001", "Om", 5000),
            new Account("AC0002", "Raj", 8000),
            new Account("AC0003", "Amit", 3000)
        };

        TransactionLog.append(logsFolder.resolve("transactions.txt"),
                "DEPOSIT 2000");
        TransactionLog.append(logsFolder.resolve("transactions.txt"),
                "WITHDRAW 500");
        TransactionLog.append(logsFolder.resolve("transactions.txt"),
                "DEPOSIT 1000");
        TransactionLog.append(logsFolder.resolve("transactions.txt"),
                "WITHDRAW 300");

        StatePersister.save(accounts, dataFile);

        Account[] loadedAccounts = StatePersister.load(dataFile);

        System.out.println("Accounts loaded from file:");
        for (Account account : loadedAccounts) {
            System.out.println(account);
        }

        ReportGenerator.generate(logsFolder, reportFile);

        System.out.println("\nEnd-of-Day Report:");
        System.out.println(
                new String(Files.readAllBytes(reportFile)));
    }
}
