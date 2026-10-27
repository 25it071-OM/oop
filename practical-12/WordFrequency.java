
import java.util.*;

public class WordFrequency {
    public static void main(String[] args) {
        String paragraph = "java is easy and java is powerful";

        Map<String, Integer> map = new HashMap<>();

        for (String word : paragraph.split("\\s+")) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }

        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(map.entrySet());

        list.sort((a, b) -> {
            int result = b.getValue().compareTo(a.getValue());
            return result != 0 ? result : a.getKey().compareTo(b.getKey());
        });

        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
