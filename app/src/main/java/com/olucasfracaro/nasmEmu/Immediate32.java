package com.olucasfracaro.nasmEmu;

public final class Immediate32 implements Operand32 {

    private final int value;

    public Immediate32(int value) {
        this.value = value;
    }

    @Override
    public int get() {
        return value;
    }
}
