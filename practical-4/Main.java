public class Main {

    public static void main(String[] args) {

        // Mobile
        System.out.println("Mobile:");
        System.out.println(
                Validator.isValidMobile("9876543210")); // true
        System.out.println(
                Validator.isValidMobile("1234567890")); // false

        // Email
        System.out.println("\nEmail:");
        System.out.println(
                Validator.isValidEmail("om@gmail.com")); // true
        System.out.println(
                Validator.isValidEmail("om@gmail")); // false

        // PAN
        System.out.println("\nPAN:");
        System.out.println(
                Validator.isValidPan("ABCDE1234F")); // true
        System.out.println(
                Validator.isValidPan("ABC1234F")); // false

        // IFSC
        System.out.println("\nIFSC:");
        System.out.println(
                Validator.isValidIfsc("SBIN0123456")); // true
        System.out.println(
                Validator.isValidIfsc("SBI123456")); // false

        // Command Parser
        System.out.println("\nCommand:");

        Command cmd =
                CommandParser.parse("DEPOSIT AC0001 500");

        System.out.println("Type: " + cmd.type());
        System.out.println("Account: " + cmd.accountNumber());
        System.out.println("Amount: " + cmd.amount());

        // Statement
        Account account =
                new Account(1, "Om", 5000);

        System.out.println("\nStatement:");
        System.out.println(
                StatementFormatter.buildStatement(account));
    }
}