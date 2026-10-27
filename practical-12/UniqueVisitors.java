
import java.util.*;

public class UniqueVisitors {
    public static void main(String[] args) {
        List<String> logins = Arrays.asList(
            "Om", "Raj", "Amit", "Om", "Neha", "Raj", "Jay"
        );

        HashSet<String> set = new HashSet<>(logins);
        LinkedHashSet<String> unique = new LinkedHashSet<>(logins);

        System.out.println("Unique users: " + set.size());

        System.out.println("First three distinct names:");
        int count = 0;

        for (String name : unique) {
            if (count == 3)
                break;

            System.out.println(name);
            count++;
        }
    }
}
