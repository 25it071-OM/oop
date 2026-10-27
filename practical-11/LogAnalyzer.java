
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class LogAnalyzer {
    public static void main(String[] args) throws IOException {
        Files.write(Paths.get("log1.txt"),
            ("INFO Server started\nERROR File missing\nINFO Ready\n")
            .getBytes());

        Files.write(Paths.get("log2.txt"),
            ("WARNING Low memory\nERROR Connection failed\n")
            .getBytes());

        String[] files = {"log1.txt", "log2.txt"};
        String keyword = "ERROR";

        int totalLines = 0;
        int keywordLines = 0;

        for (String file : files) {
            Path path = Paths.get(file);

            try (Stream<String> lines = Files.lines(path)) {
                long[] counts = lines.mapToLong(line -> {
                    return 1;
                }).toArray();

                totalLines += counts.length;
            }

            try (Stream<String> lines = Files.lines(path)) {
                keywordLines += (int) lines
                    .filter(line -> line.contains(keyword))
                    .count();
            }

            System.out.println(file + " size: " +
                Files.size(path) + " bytes");
        }

        System.out.println("Total lines: " + totalLines);
        System.out.println("Lines containing " + keyword + ": "
            + keywordLines);
    }
}
