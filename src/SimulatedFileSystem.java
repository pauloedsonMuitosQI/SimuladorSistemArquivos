import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

public class SimulatedFileSystem implements Serializable {
    private SimDirectory root = new SimDirectory("root");
    private static final long serialVersionUID = 1L;

    public void listar() {
        System.out.println("Conteúdo do diretório " + root.getName() + ":");
        Map<String, SimDirectory> dirs = root.getDirectories();
        Map<String, SimFile> files = root.getFiles();

        if (dirs.isEmpty() && files.isEmpty()) {
            System.out.println(" (vazio) ");
            return;
        }
        for (String dirName : dirs.keySet()) {
            System.out.println("[DIR]  " + dirName);
        }
        for (String fileName : files.keySet()) {
            System.out.println("[FILE] " + fileName);
        }
    }

    public void criarDiretorio(String nome) {
        Map<String, SimDirectory> dirs = root.getDirectories();
        Map<String, SimFile> files = root.getFiles();

        if (dirs.containsKey(nome) || files.containsKey(nome)) {
            System.out.println("Erro: já existe arquivo ou diretório com este nome.");
            return;
        }
        dirs.put(nome, new SimDirectory(nome));
        Journal.log("CRIAR_DIRETORIO", nome); // Chamada estática
        System.out.println("Diretório '" + nome + "' criado.");
    }

    public void apagarDiretorio(String nome) {
        Map<String, SimDirectory> dirs = root.getDirectories();
        SimDirectory dir = dirs.get(nome);
        if (dir == null) {
            System.out.println("Erro: diretório não encontrado.");
            return;
        }
        if (!dir.getDirectories().isEmpty() || !dir.getFiles().isEmpty()) {
            System.out.println("Erro: diretório não está vazio.");
            return;
        }
        dirs.remove(nome);
        Journal.log("Apagado diretório: " + nome);
        System.out.println("Diretório '" + nome + "' apagado.");
    }

    public void renomearDiretorio(String antigo, String novo) {
        Map<String, SimDirectory> dirs = root.getDirectories();
        Map<String, SimFile> files = root.getFiles();

        if (!dirs.containsKey(antigo)) {
            System.out.println("Erro: diretório antigo não encontrado.");
            return;
        }
        if (dirs.containsKey(novo) || files.containsKey(novo)) {
            System.out.println("Erro: nome novo já existe.");
            return;
        }
        SimDirectory dir = dirs.remove(antigo);
        dir.setName(novo);
        dirs.put(novo, dir);
        Journal.log("Renomeado diretório: " + antigo + " -> " + novo);
        System.out.println("Diretório '" + antigo + "' renomeado para '" + novo + "'.");
    }

    public void criarArquivo(String nome) {
        Map<String, SimFile> files = root.getFiles();
        Map<String, SimDirectory> dirs = root.getDirectories();

        if (files.containsKey(nome) || dirs.containsKey(nome)) {
            System.out.println("Erro: já existe arquivo ou diretório com este nome.");
            return;
        }
        files.put(nome, new SimFile(nome));
        Journal.log("Criado arquivo: " + nome);
        System.out.println("Arquivo '" + nome + "' criado.");
    }

    public void apagarArquivo(String nome) {
        Map<String, SimFile> files = root.getFiles();

        if (!files.containsKey(nome)) {
            System.out.println("Erro: arquivo não encontrado.");
            return;
        }
        files.remove(nome);
        Journal.log("Apagado arquivo: " + nome);
        System.out.println("Arquivo '" + nome + "' apagado.");
    }

    public void renomearArquivo(String antigo, String novo) {
        Map<String, SimFile> files = root.getFiles();
        Map<String, SimDirectory> dirs = root.getDirectories();

        if (!files.containsKey(antigo)) {
            System.out.println("Erro: arquivo antigo não encontrado.");
            return;
        }
        if (files.containsKey(novo) || dirs.containsKey(novo)) {
            System.out.println("Erro: nome novo já existe.");
            return;
        }
        SimFile f = files.remove(antigo);
        f.setName(novo);
        files.put(novo, f);
        Journal.log("Renomeado arquivo: " + antigo + " -> " + novo);
        System.out.println("Arquivo '" + antigo + "' renomeado para '" + novo + "'.");
    }

    public void copiarArquivo(String origem, String destino) {
        Map<String, SimFile> files = root.getFiles();
        Map<String, SimDirectory> dirs = root.getDirectories();

        SimFile fOrigem = files.get(origem);
        if (fOrigem == null) {
            System.out.println("Erro: arquivo origem não encontrado.");
            return;
        }
        if (files.containsKey(destino) || dirs.containsKey(destino)) {
            System.out.println("Erro: arquivo destino já existe.");
            return;
        }
        SimFile copia = new SimFile(destino);
        copia.setContent(fOrigem.getContent());
        files.put(destino, copia);
        Journal.log("Copiado arquivo: " + origem + " -> " + destino);
        System.out.println("Arquivo '" + origem + "' copiado para '" + destino + "'.");
    }

    private SimDirectory encontrarDiretorio(String caminho) {
        if (caminho.equals("root")) {
            return root;
        }

        // Implementação simples para diretórios imediatamente dentro do root
        // Para uma implementação mais completa, precisaríamos lidar com caminhos como "dir1/dir2"
        return root.getDirectories().get(caminho);
    }

    public void criarArquivoEmDiretorio(String diretorio, String nomeArquivo) {
        SimDirectory dir = encontrarDiretorio(diretorio);
        if (dir == null) {
            System.out.println("Erro: diretório '" + diretorio + "' não encontrado.");
            return;
        }

        Map<String, SimFile> files = dir.getFiles();
        if (files.containsKey(nomeArquivo)) {
            System.out.println("Erro: já existe arquivo com este nome no diretório.");
            return;
        }

        files.put(nomeArquivo, new SimFile(nomeArquivo));
        Journal.log("Criado arquivo '" + nomeArquivo + "' no diretório '" + diretorio + "'");
        System.out.println("Arquivo '" + nomeArquivo + "' criado no diretório '" + diretorio + "'.");
    }

    public void listarDiretorio(String nomeDiretorio) {
        if (nomeDiretorio.equals("root")) {
            listar(); // Reutiliza o método existente para o root
            return;
        }

        SimDirectory dir = encontrarDiretorio(nomeDiretorio);
        if (dir == null) {
            System.out.println("Erro: diretório '" + nomeDiretorio + "' não encontrado.");
            return;
        }

        System.out.println("Conteúdo do diretório '" + dir.getName() + "':");
        Map<String, SimDirectory> dirs = dir.getDirectories();
        Map<String, SimFile> files = dir.getFiles();

        if (dirs.isEmpty() && files.isEmpty()) {
            System.out.println(" (vazio) ");
            return;
        }

        for (String dirName : dirs.keySet()) {
            System.out.println("[DIR]  " + dirName);
        }
        for (String fileName : files.keySet()) {
            System.out.println("[FILE] " + fileName);
        }
    }

    public void executarComando(String comando) {
        String[] partes = comando.split("\\s+");
        String cmd = partes[0].toLowerCase();

        try {
            switch (cmd) {
                case "listar":
                    listar();
                    break;

                case "criar_diretorio":
                    criarDiretorio(partes[1]);
                    break;

                case "apagar_diretorio":
                    apagarDiretorio(partes[1]);
                    break;

                case "renomear_diretorio":
                    renomearDiretorio(partes[1], partes[2]);
                    break;

                case "criar_arquivo":
                    criarArquivo(partes[1]);
                    break;

                case "apagar_arquivo":
                    apagarArquivo(partes[1]);
                    break;

                case "renomear_arquivo":
                    renomearArquivo(partes[1], partes[2]);
                    break;

                case "copiar_arquivo":
                    copiarArquivo(partes[1], partes[2]);
                    break;

                case "mostrar_journal":
                    Journal.printLog();
                    break;

                case "ajuda":
                    mostrarAjuda();
                    break;

                case "criar_arquivo_em":
                    criarArquivoEmDiretorio(partes[1], partes[2]);
                    break;

                case "listar_diretorio":
                    listarDiretorio(partes[1]);
                    break;

                default:
                    System.out.println("Comando desconhecido. Digite 'ajuda' para opções.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Erro: argumentos insuficientes para '" + cmd + "'");
        }
    }

    private void mostrarAjuda() {
        System.out.println("Comandos disponíveis:");
        System.out.println(" listar - Lista conteúdo do diretório atual");
        System.out.println(" criar_diretorio <nome> - Cria novo diretório");
        System.out.println(" apagar_diretorio <nome> - Remove diretório vazio");
        System.out.println(" renomear_diretorio <antigo> <novo> - Renomeia diretório");
        System.out.println(" criar_arquivo <nome> - Cria novo arquivo");
        System.out.println(" apagar_arquivo <nome> - Remove arquivo");
        System.out.println(" renomear_arquivo <antigo> <novo> - Renomeia arquivo");
        System.out.println(" copiar_arquivo <origem> <destino> - Copia arquivo");
        System.out.println(" listar_diretorio <nome> - Lista conteúdo de um diretório específico");
        System.out.println(" criar_arquivo_em <diretorio> <nome> - Cria arquivo em diretório específico");
        System.out.println(" mostrar_journal - Exibe log de operações");
        System.out.println(" ajuda - Mostra esta ajuda");
    }
}