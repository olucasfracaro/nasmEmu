package com.olucasfracaro.nasmEmu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Principal
{
	public static void main(String[] args) {
        if (args.length > 0) {
            System.out.printf("Usando o código Assembly: %s", args[0]);
            String conteudo = lerArquivo(args[0]);
            Lexer lexer = new Lexer(conteudo);
            
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
            System.out.println(lexer);
        }
        else if (opc.equals("2")) {
            String arquivo = args.length > 0 ? args[0] : "teste2.asm";
            System.out.printf("Usando o código Assembly: %s", arquivo);
            String conteudo = lerArquivo(arquivo);
            Lexer lexer = new Lexer(conteudo);
            
            System.out.println(lexer);
        }
	}

    public static String lerArquivo(String arquivo) {
        String path = "/home/positivo/prog/nasmEmu/app/src/main/java/com/olucasfracaro/nasmEmu/" + arquivo;

        StringBuilder content = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
