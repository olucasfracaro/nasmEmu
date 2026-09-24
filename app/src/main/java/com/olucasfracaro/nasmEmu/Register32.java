package com.olucasfracaro.nasmEmu;

public final class Register32 implements WritableOperand32 {

    public enum Nome {
        EAX,
        EBX,
        ECX,
        EDX,
        ESI,
        EDI,
        EBP,
        ESP
    }

    private final Nome nome;

    public Register32(Nome nome) {
        this.nome = nome;
    }

    public Nome getNome() {
        return nome;
    }

    @Override
    public int get(CPU32 cpu) {
        return cpu.getRegister(nome);
    }

    @Override
    public void set(CPU32 cpu, int value) {
        cpu.setRegister(nome, value);
    }
}