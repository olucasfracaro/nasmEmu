package com.olucasfracaro.nasmEmu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Principal
{
    private static final Path DIRETORIO_ASSEMBLY =
            Path.of("src/main/java/com/olucasfracaro/nasmEmu");

	public static void main(String[] args) {
        if (args.length > 0) {
            System.out.printf("Usando o código Assembly: %s", args[0]);
            String conteudo = lerArquivo(args[0]);
            Lexer lexer = new Lexer(conteudo);
            lexer.tokenizar();

            System.out.println(lexer);
            return;
        }

        System.out.println("Escolha uma opção:");
        System.out.println("1. Digitar código Assembly");
        System.out.println("2. Ler código Assembly de um arquivo");

        Scanner scanner = new Scanner(System.in);
        String opc = scanner.nextLine();

        if (opc.equals("1")) {
            System.out.println("Digite o código Assembly (pressione Enter duas vezes para finalizar):");
            StringBuilder conteudo = new StringBuilder();
            String linha;
            while (true) {
                linha = scanner.nextLine();
                if (linha.isEmpty()) {
                    break;
                }
                conteudo.append(linha).append("\n");
            }
            scanner.close();
            Lexer lexer = new Lexer(conteudo.toString());
            lexer.tokenizar();

            System.out.println(lexer);
        }
        else if (opc.equals("2")) {
            String arquivo = args.length > 0 ? args[0] : "teste2.asm";
            System.out.printf("Usando o código Assembly: %s", arquivo);
            String conteudo = lerArquivo(arquivo);
            Lexer lexer = new Lexer(conteudo);
            lexer.tokenizar();
            
            System.out.println(lexer);
        }
	}

    public static String lerArquivo(String arquivo) {
        Path caminho = Path.of(arquivo);
        if (!caminho.isAbsolute() && caminho.getNameCount() == 1) {
            caminho = DIRETORIO_ASSEMBLY.resolve(caminho);
        }

        try {
            return Files.readString(caminho);
        } catch (IOException | RuntimeException e) {
            System.err.printf("Não foi possível ler o arquivo '%s': %s%n", arquivo, e.getMessage());
            return "";
        }
    }
}
