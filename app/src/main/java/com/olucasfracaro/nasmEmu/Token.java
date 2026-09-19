package com.olucasfracaro.nasmEmu;

public class Token {
    private Tipo tipo;
    private String valor;
    private int linha;

    public Token(Tipo tipo, String valor, int linha) {
        this.tipo = tipo;
        this.valor = valor;
        this.linha = linha;
    }

    public Tipo getTipo() { return this.tipo; }
    public String getValor() { return this.valor; }
    public int getLinha() { return this.linha; }

    @Override
    public String toString() {
        return String.format("Token{tipo='%s', valor='%s', linha=%s}", tipo, valor, linha);
    }
}