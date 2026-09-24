package com.olucasfracaro.nasmEmu;

public interface WritableOperand32 extends Operand32 {
    void set(CPU32 cpu, int value);
}