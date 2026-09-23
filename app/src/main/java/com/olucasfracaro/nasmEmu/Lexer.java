package com.olucasfracaro.nasmEmu;

import java.util.ArrayList;

public class Lexer {
    private String codigo;
    private ArrayList<Token> tokens = new ArrayList<>();

    public Lexer(String codigo) {
        this.codigo = codigo;
    }

    public ArrayList<Token> tokenizar() {
        String[] linhas = this.codigo.split("\n");

        int numeroLinha = 1;

        for (String linha : linhas) {

            String linhaSemComentario = linha.contains(";") ? this.extrairComentario(linha) : linha;

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
                tokens.add(
                    new Token(
                        Tipo.DOIS_PONTOS,
                        ":",
                        numeroLinha
                    )
                );

                if (partesLabel.length > 1) {
                    linhaSemComentario = partesLabel[1].trim();

                    if (linhaSemComentario.isEmpty()) {
                        tokens.add(
                            new Token(
                                Tipo.FIM_LINHA,
                                "\\n",
                                numeroLinha
                            )
                        );
                        numeroLinha++;
                        continue;
                    }
                } else {
                    numeroLinha++;
                    continue;
                }
            }

            ArrayList<String> partes = separarPalavras(linhaSemComentario);

            //.bss e .data
            if (partes.size() >= 2 && Analisador.isDiretiva(partes.get(1))) {

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

                    String[] listaOperandos = separarOperandos(operandos);

                    for (String operando : listaOperandos) {

                        operando = operando.trim();

                        if (operando.isEmpty()) {
                            continue;
                        }

                        Tipo tipoOperando;
                        if (operando.equals(","))                       tipoOperando = Tipo.VIRGULA;
                        else if (Analisador.isNumero(operando))         tipoOperando = Tipo.NUMERO;
                        else if (Analisador.isTexto(operando))          tipoOperando = Tipo.TEXTO;
                        else if (Analisador.isRegistrador(operando))    tipoOperando = Tipo.REGISTRADOR;
                        else                                            tipoOperando = Tipo.IDENTIFICADOR;

                        tokens.add(
                            new Token(
                                tipoOperando,
                                operando,
                                numeroLinha
                            )
                        );
                    }
                }
                tokens.add(
                    new Token(
                        Tipo.FIM_LINHA,
                        "\\n",
                        numeroLinha
                    )
                );

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
            if (partes.size() >= 1) {
                for (String operando : partes.subList(1, partes.size())) {

                    operando = operando.trim();

                    if (operando.isEmpty()) continue;

                    Tipo tipoOperando = Tipo.IDENTIFICADOR;

                    if (operando.equals(","))                   tipoOperando = Tipo.VIRGULA;
                    else if (operando.equals(":"))              tipoOperando = Tipo.DOIS_PONTOS;
                    else if (operando.equals("["))              tipoOperando = Tipo.ABRE_COLCHETE;
                    else if (operando.equals("]"))              tipoOperando = Tipo.FECHA_COLCHETE;
                    else if (Analisador.isOperador(operando))   tipoOperando = Tipo.OPERADOR;
                    else if (Analisador.isNumero(operando))     tipoOperando = Tipo.NUMERO;
                    else if (Analisador.isRegistrador(operando))tipoOperando = Tipo.REGISTRADOR;
                    else if (Analisador.isInstrucao(operando))  tipoOperando = Tipo.INSTRUCAO;
                    else if (Analisador.isDiretiva(operando))   tipoOperando = Tipo.DIRETIVA;
                    else if (Analisador.isTexto(operando))      tipoOperando = Tipo.TEXTO;
                    else                                        tipoOperando = Tipo.IDENTIFICADOR;

                    tokens.add(
                        new Token(
                            tipoOperando,
                            operando,
                            numeroLinha
                        )
                    );
                }
            }
            tokens.add(
                new Token(
                    Tipo.FIM_LINHA,
                    "\\n",
                    numeroLinha
                )
            );

            numeroLinha++;
        }

        return tokens;
    }

    private String extrairComentario(String linhaSemComentario) {
        boolean dentroDeString = false;
        char aspasAbertura = 0;

        for (int i = 0; i < linhaSemComentario.length(); i++) {
            char c = linhaSemComentario.charAt(i);

            //suporte tanto a " quanto a '
            if ((c == '"' || c == '\'') && (!dentroDeString || c == aspasAbertura)) {
                dentroDeString = !dentroDeString;
                aspasAbertura = dentroDeString ? c : 0;
            }

            if (c == ';' && !dentroDeString) {
                linhaSemComentario = linhaSemComentario.substring(0, i);
                break;
            }
        }
        return linhaSemComentario;
    }

    private Tipo identificarPalavra(String palavra) {
        String p = palavra.toLowerCase();

        if (Analisador.isInstrucao(p))      return Tipo.INSTRUCAO;
        if (Analisador.isDiretiva(p))       return Tipo.DIRETIVA;
        if (Analisador.isOperador(p))       return Tipo.OPERADOR;
        if (Analisador.isRegistrador(p))    return Tipo.REGISTRADOR;

        return Tipo.IDENTIFICADOR;
    }

    private ArrayList<String> separarPalavras(String linha) {
        ArrayList<String> palavras = new ArrayList<>();
        StringBuilder atual = new StringBuilder();

        boolean dentroDeString = false;
        char aspasAbertura = 0;

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);

            // Controle de Strings com ' ou "
            if ((c == '"' || c == '\'') && (!dentroDeString || c == aspasAbertura)) {
                dentroDeString = !dentroDeString;
                aspasAbertura = dentroDeString ? c : 0;
                atual.append(c);
                continue;
            }

            if (dentroDeString) {
                atual.append(c);
                continue;
            }

            // Símbolos delimitadores
            if (c == ',' || c == ':' || c == '[' || c == ']') {
                if (atual.length() > 0) {
                    palavras.add(atual.toString());
                    atual.setLength(0);
                }
                palavras.add(String.valueOf(c));
                continue;
            }

            // Operadores de 2 caracteres (<< e >>)
            if ((c == '<' || c == '>') && i + 1 < linha.length() && linha.charAt(i + 1) == c) {
                if (atual.length() > 0) {
                    palavras.add(atual.toString());
                    atual.setLength(0);
                }
                palavras.add(linha.substring(i, i + 2));
                i++; // Pula o caractere repetido
                continue;
            }

            // Operadores de 1 caractere ou sinal negativo
            if (Analisador.isOperador(String.valueOf(c))) {
                if (c == '-' && atual.length() == 0 && i + 1 < linha.length() && Character.isDigit(linha.charAt(i + 1))) {
                    atual.append(c);
                } else {
                    if (atual.length() > 0) {
                        palavras.add(atual.toString());
                        atual.setLength(0);
                    }
                    palavras.add(String.valueOf(c));
                }
                continue;
            }

            // Espaços
            if (Character.isWhitespace(c)) {
                if (atual.length() > 0) {
                    palavras.add(atual.toString());
                    atual.setLength(0);
                }
                continue;
            }

            atual.append(c);
        }

        if (atual.length() > 0) {
            palavras.add(atual.toString());
        }

        return palavras;
    }

    private String[] separarOperandos(String operandos) {
        ArrayList<String> lista = new ArrayList<>();
        StringBuilder atual = new StringBuilder();

        boolean dentroDeString = false;
        char aspasAbertura = 0;

        for (int i = 0; i < operandos.length(); i++) {
            char c = operandos.charAt(i);

            if ((c == '"' || c == '\'') && (!dentroDeString || c == aspasAbertura)) {
                dentroDeString = !dentroDeString;
                aspasAbertura = dentroDeString ? c : 0;
                atual.append(c);
            }
            else if (c == ',' && !dentroDeString) {
                if (atual.length() > 0) {
                    lista.add(atual.toString().trim());
                    atual.setLength(0);
                }
                lista.add(",");
            }
            else {
                atual.append(c);
            }
        }

        if (atual.length() > 0) {
            lista.add(atual.toString().trim());
        }

        return lista.toArray(new String[0]);
    }

    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getCodigo() { return this.codigo; }

    @Override
    public String toString() {
        return String.join(
            "\n",
            tokens.stream()
                .map(token -> token.toString())
                .toArray(String[]::new)
        );
    }
}