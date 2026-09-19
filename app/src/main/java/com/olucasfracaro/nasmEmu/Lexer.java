package com.olucasfracaro.nasmEmu;

import java.util.ArrayList;

public class Lexer {
    private String codigo;

    private ArrayList<Token> tokens = new ArrayList<>();

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Lexer(String codigo) {
        this.codigo = codigo;
        this.tokenizar();
    }

    public ArrayList<Token> tokenizar() {
        String[] linhas = this.codigo.split("\n");

        int numeroLinha = 1;

        for (String linha : linhas) {

            String linhaSemComentario = linha;

            if (linhaSemComentario.contains(";")) {
                linhaSemComentario =
                    linhaSemComentario.substring(0, linhaSemComentario.indexOf(";"));
            }

            linhaSemComentario = linhaSemComentario.trim();

            if (linhaSemComentario.isEmpty()) {
                numeroLinha++;
                continue;
            }

            //Tipo.LABEL
            if (linhaSemComentario.contains(":")) {

                String[] partesLabel = linhaSemComentario.split(":", 2);

                String label = partesLabel[0].trim();

                tokens.add(
                    new Token(
                        Tipo.LABEL,
                        label,
                        numeroLinha
                    )
                );

                if (partesLabel.length > 1) {
                    linhaSemComentario = partesLabel[1].trim();

                    if (linhaSemComentario.isEmpty()) {
                        numeroLinha++;
                        continue;
                    }
                } else {
                    numeroLinha++;
                    continue;
                }
            }

            ArrayList<String> partes = separarPalavras(linhaSemComentario);

            // ---------------------------------------------------------
            // DECLARAÇÃO DE DADOS
            //
            // Exemplo:
            // buffer resb 64
            // result resd 1
            // value dd 42
            // msg db "Hello", 10, 0
            // ---------------------------------------------------------

            if (partes.size() >= 2 && isDiretiva(partes.get(1))) {

                //Tipo.IDENTIFICADOR
                tokens.add(
                    new Token(
                        Tipo.IDENTIFICADOR,
                        partes.get(0),
                        numeroLinha
                    )
                );

                //Tipo.DIRETIVA
                tokens.add(
                    new Token(
                        Tipo.DIRETIVA,
                        partes.get(1).toUpperCase(),
                        numeroLinha
                    )
                );

                //operandos da Tipo.DIRETIVA
                if (partes.size() > 2) {

                    String operandos = String.join(
                        " ",
                        partes.subList(2, partes.size())
                    );

                    String[] listaOperandos = operandos.split(",");

                    for (String operando : listaOperandos) {

                        operando = operando.trim();

                        if (operando.isEmpty()) {
                            continue;
                        }

                        Tipo tipoOperando;

                        if (operando.matches("-?(\\d+|0[xX][0-9a-fA-F]+)")) {
                            tipoOperando = Tipo.NUMERO;
                        }
                        else if (operando.startsWith("\"")
                              && operando.endsWith("\"")) {
                            tipoOperando = Tipo.TEXTO;
                        }
                        else if (isRegistrador(operando)) {
                            tipoOperando = Tipo.REGISTRADOR;
                        }
                        else {
                            tipoOperando = Tipo.IDENTIFICADOR;
                        }

                        tokens.add(
                            new Token(
                                tipoOperando,
                                operando,
                                numeroLinha
                            )
                        );
                    }
                }

                numeroLinha++;
                continue;
            }

            //Tipo.INSTRUCAO, Tipo.DIRETIVA, Tipo.IDENTIFICADOR
            String palavra = partes.get(0).toUpperCase();

            Tipo tipo = identificarPalavra(palavra);

            tokens.add(
                new Token(
                    tipo,
                    palavra,
                    numeroLinha
                )
            );

            //OPERANDOS
            if (partes.size() > 1) {

                String operandos = String.join(
                    " ",
                    partes.subList(2, partes.size())
                );

                String[] listaOperandos = operandos.split(",");

                for (String operando : listaOperandos) {

                    operando = operando.trim();

                    if (operando.isEmpty()) {
                        continue;
                    }

                    Tipo tipoOperando;

                    if (operando.matches("-?(\\d+|0[xX][0-9a-fA-F]+)")) {
                        tipoOperando = Tipo.NUMERO;
                    }
                    else if (isRegistrador(operando)) {
                        tipoOperando = Tipo.REGISTRADOR;
                    }
                    else if (isInstrucao(operando)) {
                        tipoOperando = Tipo.INSTRUCAO;
                    }
                    else if (isDiretiva(operando)) {
                        tipoOperando = Tipo.DIRETIVA;
                    }
                    else if (operando.startsWith("\"")
                          && operando.endsWith("\"")) {
                        tipoOperando = Tipo.TEXTO;
                    }
                    else {
                        tipoOperando = Tipo.IDENTIFICADOR;
                    }

                    tokens.add(
                        new Token(
                            tipoOperando,
                            operando,
                            numeroLinha
                        )
                    );
                }
            }

            numeroLinha++;
        }

        return tokens;
    }

    private boolean isInstrucao(String palavra) {

        palavra = palavra.toLowerCase();

        return switch (palavra) {
            case "mov", "lea",
                "add", "sub",
                "inc", "dec", "neg",
                "imul",
                "and", "or", "xor", "not",
                "shl", "shr", "rol", "ror",
                "cmp", "test",
                "je", "jne", "jmp",
                "push", "pop", "xchg",
                "loop",
                "nop",
                "div", "idiv",
                "cdq", "cwd",
                "int" -> true;

            default -> false;
        };
    }

    private boolean isDiretiva(String palavra) {

        palavra = palavra.toLowerCase();

        return switch (palavra) {
            case "bits",
                "global",
                "section",
                "db",
                "dw",
                "dd",
                "dq",
                "equ",
                "resb",
                "resw",
                "resd",
                "resq" -> true;

            default -> false;
        };
    }

    private boolean isRegistrador(String palavra) {

        palavra = palavra.toLowerCase();

        return switch (palavra) {
            case "eax", "ebx", "ecx", "edx",
                "esi", "edi", "esp", "ebp",
                "ax", "bx", "cx", "dx",
                "al", "ah", "bl", "bh",
                "cl", "ch", "dl", "dh" -> true;

            default -> false;
        };
    }

    private Tipo identificarPalavra(String palavra) {

        String p = palavra.toLowerCase();

        if (isInstrucao(p)) {
            return Tipo.INSTRUCAO;
        }

        if (isDiretiva(p)) {
            return Tipo.DIRETIVA;
        }

        if (isRegistrador(p)) {
            return Tipo.REGISTRADOR;
        }

        return Tipo.IDENTIFICADOR;
    }

    private ArrayList<String> separarPalavras(String linha) {
        ArrayList<String> palavras = new ArrayList<>();

        StringBuilder atual = new StringBuilder();

        boolean dentroDeString = false;

        for (int i = 0; i < linha.length(); i++) {

            char c = linha.charAt(i);

            if (c == '"') {
                dentroDeString = !dentroDeString;
                atual.append(c);
            }
            else if (Character.isWhitespace(c) && !dentroDeString) {

                if (atual.length() > 0) {
                    palavras.add(atual.toString());
                    atual.setLength(0);
                }

            }
            else {
                atual.append(c);
            }
        }

        if (atual.length() > 0) {
            palavras.add(atual.toString());
        }

        return palavras;
    }

    @Override
    public String toString() {
        return String.join(
            "\n",
            tokens.stream()
                .map(Token::toString)
                .toArray(String[]::new)
        );
    }
}