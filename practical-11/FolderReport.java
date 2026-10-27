
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class FolderReport {
    public static void main(String[] args) throws IOException {
        Path folder = Paths.get(".");
        Path report = Paths.get("report.txt");

        try (PrintWriter writer = new PrintWriter(
                Files.newBufferedWriter(report, StandardCharsets.UTF_8))) {

            writer.println("FOLDER FILE REPORT");
            writer.println("------------------");

            Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(
                        Path file, BasicFileAttributes attrs) {

                    try {
                        writer.println("Name: " + file.getFileName());
                        writer.println("Size: " + attrs.size() + " bytes");
                        writer.println("Modified: " +
                            attrs.lastModifiedTime());
                        writer.println();
                    } catch (Exception e) {
                        System.out.println("Error: " + file);
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
        }

        System.out.println("Report saved to report.txt");
    }
}
