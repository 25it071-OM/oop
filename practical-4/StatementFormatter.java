public class StatementFormatter {

    public static String buildStatement(Account account) {

        StringBuilder sb = new StringBuilder();

        sb.append("----- Account Statement -----\n");
        sb.append("Account Number: ")
          .append(account.getAccountNumber())
          .append("\n");

        sb.append(account.toString())
          .append("\n");

        sb.append("-----------------------------");

        return sb.toString();
    }
}