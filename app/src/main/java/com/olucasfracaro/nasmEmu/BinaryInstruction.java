package com.olucasfracaro.nasmEmu;

abstract class BinaryInstruction implements Instruction {

    protected final WritableOperand32 destination;
    protected final Operand32 source;

    protected BinaryInstruction(
        WritableOperand32 destination,
        Operand32 source
    ) {
        this.destination = destination;
        this.source = source;
    }
}