import java.util.Scanner;

// Record
record BankInfo(String name, String branch) {
}

// Enum
enum MenuOption {
    OPEN_ACCOUNT,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    EXIT
}

// Main Class
public class MiniBank {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Bank information
        BankInfo bank = new BankInfo("MiniBank", "Main Branch");

        // Application header
        System.out.println("================================");
        System.out.println(bank);
        System.out.println("================================");

        boolean running = true;

        // Menu loop
        while (running) {

            System.out.println("\n===== MiniBank Menu =====");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            // Switch expression
            String message = switch (choice) {

                case 1 -> "Open Account - to be implemented in a later lab";

                case 2 -> "Deposit - to be implemented in a later lab";

                case 3 -> "Withdraw - to be implemented in a later lab";

                case 4 -> "Transfer - to be implemented in a later lab";

                case 5 -> {
                    running = false;
                    yield "Thank you for using MiniBank!";
                }

                default -> "Invalid choice";
            };

            System.out.println(message);
        }

        sc.close();
    }
}