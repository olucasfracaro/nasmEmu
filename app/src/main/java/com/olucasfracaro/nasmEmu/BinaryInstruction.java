package com.olucasfracaro.nasmEmu;

abstract class BinaryInstruction implements Instruction {

    protected final Register32 destination;
    protected final Operand32 source;

    public BinaryInstruction(
        Register32 destination,
        Operand32 source
    ) {
        this.destination = destination;
        this.source = source;
    }
}
