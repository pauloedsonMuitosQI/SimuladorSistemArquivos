import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;

public final class Journal {
    private static final String LOG_FILE = "data/journal.log";

    // Construtor privado para evitar instanciação
    private Journal() {}

    public static void log(String operation, String... params) {
        String entry = LocalDateTime.now() + "|" + operation + "|" +
                String.join("|", params);
        try {
            // Garante que o diretório existe
            Files.createDirectories(Paths.get("data"));

            Files.write(Paths.get(LOG_FILE),
                    (entry + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Erro no journal: " + e.getMessage());
        }
    }

    public static void printLog() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(LOG_FILE));
            if (lines.isEmpty()) {
                System.out.println("Journal vazio");
                return;
            }
            System.out.println("=== Conteúdo do Journal ===");
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Não foi possível ler o journal: " + e.getMessage());
        }
    }
}