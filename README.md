# Simulador de Sistema de Arquivos com Journaling

## Resumo

Este projeto consiste no desenvolvimento de um simulador de sistema de arquivos em Java, com o objetivo de demonstrar a organização e as operações fundamentais de um sistema de arquivos, incluindo o crucial mecanismo de Journaling para garantir a integridade dos dados. O simulador permite a manipulação de arquivos e diretórios através de uma interface de linha de comando (Shell), simulando comandos básicos de um sistema operacional.

## Introdução

O gerenciamento eficiente de arquivos é crucial para o funcionamento dos sistemas operacionais. Para isso, entender como é montado e organizado um sistema é a base para a compreensão dos sistemas operacionais. Este simulador foi desenvolvido para fornecer uma visão prática da estrutura de um sistema de arquivos e da importância do Journaling.

## Objetivo

Desenvolver um simulador de sistema de arquivos em Java que implemente funcionalidades básicas de manipulação de arquivos e diretórios, com suporte a Journaling para garantir a integridade dos dados. Este simulador permite a criação de um arquivo que simula o sistema de arquivos (`base.dat`) e realizar operações como:

*   Copiar arquivos e diretórios (`cp`)
*   Apagar arquivos e diretórios (`rm`)
*   Renomear arquivos e diretórios (`mv`)
*   Criar diretórios (`mkdir`)
*   Criar arquivos (`touch`)
*   Listar o conteúdo de um diretório (`ls`)
*   Mudar de diretório (`cd`)

## Metodologia

O simulador foi desenvolvido em linguagem de programação Java, utilizando classes para representar as entidades do sistema de arquivos (entradas, arquivos, diretórios) e o sistema em si. A interação ocorre através de um *loop* de comandos (modo Shell), onde cada comando invoca um método correspondente na classe principal do sistema de arquivos.

### Journaling

O Journaling é implementado através da classe `Journal`, que registra cada operação de modificação do sistema de arquivos (criação, remoção, renomeação, cópia) em um arquivo de log (`journal.log`). Este log é essencial para simular a capacidade de recuperação do sistema em caso de falha, garantindo que as operações sejam atômicas e persistentes.

## Parte 1: Introdução ao Sistema de Arquivos com Journaling

### Descrição do Sistema de Arquivos

Um **sistema de arquivos** é a estrutura lógica usada por um sistema operacional para controlar como os dados são armazenados e recuperados. Sem um sistema de arquivos, as informações colocadas em um meio de armazenamento seriam apenas um grande bloco de dados sem significado. Ele organiza os dados em arquivos e os arquivos em grupos de diretórios (ou pastas), gerenciando o espaço livre e as permissões de acesso.

### Journaling

**Journaling** (ou registro em diário) é uma técnica crucial para garantir a **integridade dos dados** em sistemas de arquivos. Ele funciona registrando as alterações que serão feitas no sistema de arquivos em um log (o "journal") antes de realmente aplicá-las aos dados.

O **propósito** principal é a **recuperação rápida** após uma falha (como queda de energia ou *crash* do sistema). Em vez de ter que escanear todo o sistema de arquivos (o que é demorado), o sistema pode simplesmente ler o journal e refazer ou desfazer as operações incompletas.

**Tipos de Journaling:**

| Tipo | Descrição | Vantagens | Desvantagens |
| :--- | :--- | :--- | :--- |
| **Writeback** (Dados e Metadados) | Apenas os metadados são registrados no journal. Os dados podem ser escritos antes ou depois dos metadados. | Melhor desempenho. | Maior risco de inconsistência de dados (o arquivo pode conter dados antigos após a recuperação). |
| **Ordered** (Metadados) | Apenas os metadados são registrados. Garante que os dados sejam escritos no disco antes que os metadados sejam registrados no journal. | Bom equilíbrio entre desempenho e segurança. | Risco de dados antigos serem lidos se a falha ocorrer após a escrita dos dados, mas antes da atualização dos metadados. |
| **Data** (Dados e Metadados) | Tanto os metadados quanto os dados são registrados no journal. | Maior segurança e integridade dos dados. | Pior desempenho devido à escrita duplicada (no journal e no local final). |

O simulador implementa uma forma simplificada de Journaling, registrando a operação e seus detalhes no `journal.log` antes de executar a mudança no sistema de arquivos simulado.

## Parte 2: Arquitetura do Simulador

### Estrutura de Dados

O simulador utiliza uma estrutura de dados baseada em classes Java para representar a hierarquia do sistema de arquivos:

| Classe | Descrição |
| :--- | :--- |
| `SimEntry` | Classe abstrata base. Contém atributos comuns a arquivos e diretórios (nome, tempo de criação, tempo de modificação, referência ao diretório pai). Implementa `Serializable` para persistência. |
| `SimFile` | Estende `SimEntry`. Representa um arquivo. Contém um campo `content` (String, simplificado). |
| `SimDirectory` | Estende `SimEntry`. Representa um diretório. Contém um `Map<String, SimEntry>` para armazenar seus filhos (arquivos e subdiretórios). |
| `SimulatedFileSystem` | Classe principal que gerencia a estrutura. Contém a referência ao diretório raiz (`root`) e implementa toda a lógica das operações (criação, remoção, navegação, etc.). |
| `FileManager` | Classe utilitária responsável por salvar e carregar o estado do `SimulatedFileSystem` para o arquivo `base.dat` usando serialização Java. |
| `Journal` | Classe utilitária estática para registrar as operações no `journal.log`. |

## Parte 3: Implementação em Java

O código está organizado nos seguintes arquivos:

*   `SimEntry.java`: Classe base abstrata.
*   `SimFile.java`: Representação de um arquivo.
*   `SimDirectory.java`: Representação de um diretório.
*   `Journal.java`: Implementação do Journaling.
*   `FileManager.java`: Lógica de persistência (salvar/carregar).
*   `SimulatedFileSystem.java`: Lógica central do sistema de arquivos.
*   `Main.java`: Interface de linha de comando (Shell).

## Parte 4: Instalação e Funcionamento

### Recursos Usados

*   Linguagem: Java (JDK 17 ou superior)
*   Ferramentas: `javac` (compilador Java), `java` (máquina virtual Java)
*   Mecanismos: Serialização Java (para persistência e cópia profunda), `java.io` (para Journaling e persistência).

### Orientações sobre a Execução do Simulador

1.  **Compilação:**
    Navegue até o diretório raiz do projeto (onde estão as pastas `src`, `out`, etc.) e compile os arquivos Java:

    ```bash
    javac -d out -sourcepath src src/*.java
    ```

2.  **Execução:**
    Execute o programa principal a partir do diretório raiz:

    ```bash
    java -cp out Main
    ```

3.  **Interação (Modo Shell):**
    O simulador iniciará no modo Shell, onde você pode interagir com os comandos.

### Exemplo Prático de Execução

A seguir, um exemplo de sessão de comandos que demonstra as principais funcionalidades do simulador, incluindo a criação de diretórios, arquivos, navegação, cópia e remoção:

| Ação/Cenário | Comando Executado | Resultado no Terminal | Saída Esperada do Sistema |
| :--- | :--- | :--- | :--- |
| **Cria o diretório `docs` na raiz.** | `criar_diretorio docs` | `/ $` | `Diretório 'docs' criado com sucesso.` |
| **Navega para o diretório `docs`.** | `cd docs` ou (assumindo que seja o próximo passo lógico) | `/docs $` | `Movido para /docs` (Exemplo de Saída) |
| **Cria o arquivo `relatorio.txt` dentro de `docs`.** | `criar_arquivo relatorio.txt` | `/docs $` | `Arquivo 'relatorio.txt' criado com sucesso.` |
| **Cria o subdiretório `rascunhos` dentro de `docs`.** | `criar_diretorio rascunhos` | `/docs $` | `Diretório 'rascunhos' criado com sucesso.` |
| **Lista o conteúdo de `/docs`.** | `listar` | `/docs $` | `Conteúdo de /docs:` <br> `[DIR] rascunhos` <br> `[FILE] relatorio.txt` |
| **Retorna ao diretório raiz.** | `cd ..` ou `cd /` | `/ $` | `Movido para /` (Exemplo de Saída) |
| **Copia o diretório `docs` (e seu conteúdo) para `docs_backup`.** | `copiar_diretorio docs docs_backup` | `/ $` | `Entrada 'docs' copiada para 'docs_backup' com sucesso.` |
| **Lista o conteúdo do diretório copiado.** | `listar_diretorio docs_backup` | `/ $` | `Conteúdo de /docs_backup:` <br> `[DIR] rascunhos` <br> `[FILE] relatorio.txt` |
| **Salva o estado do sistema e encerra.** | `sair` | `/ $` | `Sistema de arquivos salvo com sucesso em base.dat` <br> `Saindo do simulador. Sistema de arquivos salvo.` |

**Verificação do Journaling:**

Após a execução, o arquivo `journal.log` será criado (ou atualizado) no diretório raiz do projeto, contendo o registro de todas as operações realizadas, demonstrando o princípio do *write-ahead logging*.



