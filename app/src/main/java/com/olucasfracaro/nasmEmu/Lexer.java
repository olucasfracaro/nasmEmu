package com.olucasfracaro.nasmEmu;

import java.util.ArrayList;

/**
 * Classe responsável por transformar um código Assembly em uma lista de tokens.
 * Esta classe é utilizada pelo @see Parser para obter os tokens do código Assembly.
 * @author Lucas M.F.
 * @see Parser
 */
public class Lexer {
    private String codigo;
    private ArrayList<Token> tokens = new ArrayList<>();

    public Lexer(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Transforma um código Assembly em uma lista de tokens.
     * @return Uma lista de tokens representando o código Assembly.
     */
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

                        Tipo tipoOperando = identificarPalavra(operando);

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

                    Tipo tipoOperando = identificarPalavra(operando);

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

    /**
     * Extrai o comentário (;) da linha de forma segura
     * @param linha A linha de código com o comentário.
     * @return A linha de código sem o comentário.
     */
    private String extrairComentario(String linha) {
        boolean dentroDeString = false;
        char aspasAbertura = 0;

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);

            //suporte tanto a " quanto a '
            if ((c == '"' || c == '\'') && (!dentroDeString || c == aspasAbertura)) {
                dentroDeString = !dentroDeString;
                aspasAbertura = dentroDeString ? c : 0;
            }

            if (c == ';' && !dentroDeString) {
                linha = linha.substring(0, i);
                break;
            }
        }
        return linha;
    }

    /**
     * Identifica o tipo de uma palavra no código Assembly.
     * @param palavra A palavra a ser identificada.
     * @return O tipo da palavra.
     */
    private Tipo identificarPalavra(String palavra) {
        String p = palavra.toLowerCase();

        if (p.equals(","))                 return Tipo.VIRGULA;
        if (p.equals(":"))                 return Tipo.DOIS_PONTOS;
        if (p.equals("["))                 return Tipo.ABRE_COLCHETE;
        if (p.equals("]"))                 return Tipo.FECHA_COLCHETE;
        if (Analisador.isNumero(p))         return Tipo.NUMERO;
        if (Analisador.isTexto(p))          return Tipo.TEXTO;
        if (Analisador.isInstrucao(p))      return Tipo.INSTRUCAO;
        if (Analisador.isDiretiva(p))       return Tipo.DIRETIVA;
        if (Analisador.isOperador(p))       return Tipo.OPERADOR;
        if (Analisador.isRegistrador(p))    return Tipo.REGISTRADOR;

        return Tipo.IDENTIFICADOR;
    }

    /**
     * Separa uma linha de código em palavras, considerando strings, delimitadores e operadores.
     * @param linha A linha de código a ser separada.
     * @return Uma lista de palavras extraídas da linha.
     */
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

    /**
     * Separa os operandos de uma instrução em uma lista de strings, considerando strings e vírgulas.
     * @param operandos A string contendo os operandos.
     * @return Um array de strings com os operandos separados.
     */
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

    //getters e setters
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getCodigo() { return this.codigo; }

    /**
     * Retorna uma representação em string do lexer, unindo todos os tokens com quebras de linha.
     * @return Uma string contendo todos os tokens separados por quebras de linha.
     */
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