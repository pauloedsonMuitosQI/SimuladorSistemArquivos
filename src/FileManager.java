import java.io.*;

public class FileManager {
    private static final String DATA_FILE = "data/base.dat";

    public static void saveSystem(SimulatedFileSystem fs) {
        try {
            // Garante que o diretório existe
            new File("data").mkdirs();

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(DATA_FILE))) {
                oos.writeObject(fs);
                Journal.log("SISTEMA_SALVO", DATA_FILE); // Registra no journal
            }
        } catch (IOException e) {
            Journal.log("ERRO_SISTEMA", "Falha ao salvar: " + e.getMessage());
            System.err.println("Erro ao salvar sistema: " + e.getMessage());
        }
    }

    public static SimulatedFileSystem loadSystem() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            Journal.log("SISTEMA_NOVO", "Arquivo base.dat não encontrado");
            return new SimulatedFileSystem();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {
            Journal.log("SISTEMA_CARREGADO", DATA_FILE);
            return (SimulatedFileSystem) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Journal.log("ERRO_SISTEMA", "Falha ao carregar: " + e.getMessage());
            System.err.println("Erro ao carregar sistema: " + e.getMessage());
            return new SimulatedFileSystem();
        }
    }
}
