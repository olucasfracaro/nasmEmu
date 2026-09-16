package com.olucasfracaro.nasmEmu;

public final class CPU32 {

    private final Register32 eax = new Register32();
    private final Register32 ebx = new Register32();
    private final Register32 ecx = new Register32();
    private final Register32 edx = new Register32();

    private final Register32 esi = new Register32();
    private final Register32 edi = new Register32();
    private final Register32 ebp = new Register32();
    private final Register32 esp = new Register32();

    public Register32 eax() { return eax; }
    public Register32 ebx() { return ebx; }
    public Register32 ecx() { return ecx; }
    public Register32 edx() { return edx; }

    public Register32 esi() { return esi; }
    public Register32 edi() { return edi; }
    public Register32 ebp() { return ebp; }
    public Register32 esp() { return esp; }

    private int eflags;

    public int getEflags() {return eflags; }
    public void setEflags(int eflags) { this.eflags = eflags; }

    
    public CPU32() {
        this.eflags = 0;
    }

    public void updateFlagsForAdd(int a, int b, int result) {

        /*
         * CF - Carry Flag
         * Bit 0 (xxxxxxxY)
         */
        long unsigned =
                Integer.toUnsignedLong(a)
                + Integer.toUnsignedLong(b);

        if ((unsigned >>> 32) != 0) {
            eflags |= (1 << 0);
        } else {
            eflags &= ~(1 << 0);
        }

        /*
         * ZF - Zero Flag
         * Bit 6 (xxxxxYxx)
         */
        if (result == 0) {
            eflags |= (1 << 6);
        } else {
            eflags &= ~(1 << 6);
        }
 
        /*
         * SF - Sign Flag
         * Bit 7 (xxxxxxYx)
         */
        if (result < 0) {
            eflags |= (1 << 7);
        } else {
            eflags &= ~(1 << 7);
        }

        /*
         * OF - Overflow Flag
         * Bit 11 ()
         */
        boolean overflow =
                ((a ^ result)
                & (b ^ result)
                & 0x80000000) != 0;

        if (overflow) {
            eflags |= (1 << 11);
        } else {
            eflags &= ~(1 << 11);
        }
    }
}

final class Xor extends BinaryInstruction {

    public Xor(Register32 destination, Operand32 source) {
        super(destination, source);
    }

    @Override
    public void execute(CPU32 cpu) {

        int a = destination.get();
        int b = source.get();

        int result = a ^ b;

        destination.set(result);

        // Update flags
        cpu.updateFlagsForAdd(a, b, result);
    }
}

final class Mov extends BinaryInstruction {

    public Mov(Register32 destination, Operand32 source) {
        super(destination, source);
    }

    @Override
    public void execute(CPU32 cpu) {
        int value = source.get();
        destination.set(value);
    }
}

final class Add extends BinaryInstruction {

    public Add(Register32 destination, Operand32 source) {
        super(destination, source);
    }

    @Override
    public void execute(CPU32 cpu) {

        int a = destination.get();
        int b = source.get();

        int result = a + b;

        destination.set(result);

        cpu.updateFlagsForAdd(a, b, result);
    }
}
