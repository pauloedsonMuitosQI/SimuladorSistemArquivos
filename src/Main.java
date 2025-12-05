import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Carrega o sistema existente
        SimulatedFileSystem fs = FileManager.loadSystem();

        // Adiciona hook para salvar ao fechar
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nSalvando sistema...");
            FileManager.saveSystem(fs);
        }));

        Scanner scanner = new Scanner(System.in);
        System.out.println("Simulador de Sistema de Arquivos com Journaling");

        while (true) {
            System.out.print("> ");
            String comando = scanner.nextLine().trim();

            if (comando.equalsIgnoreCase("sair")) {
                break;
            }

            fs.executarComando(comando);
        }

        scanner.close();
    }
}