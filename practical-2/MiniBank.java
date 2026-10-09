public class MiniBank {

    public static void main(String[] args) {

        // Customer
        Customer customer = new Customer(
                "Riya",
                "riya@gmail.com",
                "9876543210"
        );

        System.out.println("Customer ID: "
                + customer.getCustomerId());

        System.out.println("Customer Name: "
                + customer.getName());

        // Three Account objects
        Account[] accounts = {
                new Account("Riya", 5000),
                new Account("Om", 3000),
                new Account("Amit")
        };

        // Deposit
        accounts[0].deposit(2000);

        // Withdraw
        accounts[0].withdraw(3000);

        // Withdraw with insufficient balance
        boolean result = accounts[0].withdraw(10000);

        System.out.println(
                "Withdrawal of 10000: " + result
        );

        // Print accounts
        System.out.println("\n===== Accounts =====");

        for (Account account : accounts) {

            System.out.println(
                    account.getAccountNumber()
                    + " | "
                    + account.getOwnerName()
                    + " | Balance: "
                    + account.getBalance()
            );
        }
    }
}