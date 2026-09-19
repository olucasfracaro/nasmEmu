package com.olucasfracaro.nasmEmu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Principal
{
	public static void main(String[] args) {
	    String conteudo = lerArquivo("/home/positivo/prog/nasmEmu/app/src/main/java/com/olucasfracaro/nasmEmu/teste1.asm");
		Lexer lexer = new Lexer(conteudo);
		
		System.out.println(lexer);
	}

    public static String lerArquivo(String path) {
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
