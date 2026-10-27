
import java.util.*;

public class Leaderboard {
    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();

        scores.put("Om", 95);
        scores.put("Raj", 88);
        scores.put("Amit", 95);
        scores.put("Neha", 78);
        scores.put("Jay", 90);

        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(scores.entrySet());

        list.sort((a, b) -> {
            int result = b.getValue().compareTo(a.getValue());
            return result != 0 ? result : a.getKey().compareTo(b.getKey());
        });

        int topN = 3;
        System.out.println("Top " + topN + " Players:");

        for (int i = 0; i < Math.min(topN, list.size()); i++) {
            System.out.println(
                list.get(i).getKey() + " : " +
                list.get(i).getValue()
            );
        }
    }
}
