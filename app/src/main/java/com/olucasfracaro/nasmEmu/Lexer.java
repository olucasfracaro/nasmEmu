package com.olucasfracaro.nasmEmu;

import java.util.ArrayList;

public class Lexer {
    private String codigo;

    private ArrayList<String> instrucoes = new ArrayList<>();
    
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getCodigo() { return this.codigo; }
    
    public Lexer(String codigo) {
        this.codigo = codigo;
        this.tokenizar();
    }
    
    private String[] paraLinhas() {
        return this.codigo.replace("\t", "").trim().split("\n");
    }

    public ArrayList<String> tokenizar() {
        String[] linhas = this.paraLinhas();
		
		for (String linha : linhas) {
            //ignora linhas vazias e comentários
            if (linha.equals("\n") || linha.trim().isEmpty()) continue;
            //ignora comentários de linha inteira
            if (linha.trim().startsWith(";")) continue;

	        String[] partes = linha.trim().toUpperCase().split("\\s+", 2);
	        
		    String instrucao = partes[0];
            String operandos = "";
		    if (partes.length > 1) {
                operandos = partes[1];
            }
            else if (partes.length == 1) {
                this.instrucoes.add(instrucao);
                continue;
            } else {
                throw new IllegalArgumentException("Linha inválida: " + linha);
            }

            if (operandos.contains(";")) { //remove comentários
                operandos = operandos.split(";")[0];
            }
		    
		    String[] operandosLista = operandos.trim().split(",");

            if (operandosLista.length == 1) {
                String op1 = operandosLista[0].trim();
                this.instrucoes.add(String.format("%s %s", instrucao, op1));
                continue;
            }
            else if (operandosLista.length == 2) {
                String op1 = operandosLista[0].trim();
                String op2 = operandosLista[1].trim();
                this.instrucoes.add(String.format("%s %s %s", instrucao, op1, op2));
                continue;
            }
            else if (operandosLista.length == 3) {
                String op1 = operandosLista[0].trim();
                String op2 = operandosLista[1].trim();
                String op3 = operandosLista[2].trim();
                this.instrucoes.add(String.format("%s %s %s %s", instrucao, op1, op2, op3));
                continue;
            }
            else if (operandosLista.length > 3) {
                throw new IllegalArgumentException("Número de operandos inválido: " + operandosLista.length);
            }
        }
        return this.instrucoes;
    }
    
    @Override
    public String toString() {
        return String.join("\n", instrucoes);
    }
}

