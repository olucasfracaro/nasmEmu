package com.olucasfracaro.nasmEmu;

public final class CPU32 {

    // EFLAGS
    private static final int CF = 0;
    private static final int ZF = 6;
    private static final int SF = 7;
    private static final int OF = 11;

    // General-purpose registers
    private int eax;
    private int ebx;
    private int ecx;
    private int edx;

    private int esi;
    private int edi;
    private int ebp;
    private int esp;

    // EIP
    private int eip;

    // EFLAGS
    private int eflags;

    public CPU32() {
        this.eflags = 0;
        this.eip = 0;
    }

    public int getRegister(Register32.Nome nome) {
        return switch (nome) {
            case EAX -> eax;
            case EBX -> ebx;
            case ECX -> ecx;
            case EDX -> edx;
            case ESI -> esi;
            case EDI -> edi;
            case EBP -> ebp;
            case ESP -> esp;
        };
    }

    public void setRegister(Register32.Nome nome, int value) {
        switch (nome) {
            case EAX -> eax = value;
            case EBX -> ebx = value;
            case ECX -> ecx = value;
            case EDX -> edx = value;
            case ESI -> esi = value;
            case EDI -> edi = value;
            case EBP -> ebp = value;
            case ESP -> esp = value;
        }
    }

    public int eip() { return eip; }
    public void eip(int value) { this.eip = value; }

    public int getEflags() { return eflags; }
    public void setEflags(int eflags) { this.eflags = eflags; }

    /*
     * Atualiza as flags afetadas por ADD.
     */
    public void updateFlagsAdd(int a, int b, int result) {

        long unsigned =
                Integer.toUnsignedLong(a)
                + Integer.toUnsignedLong(b);

        setFlag(CF, (unsigned >>> 32) != 0);

        setFlag(ZF, result == 0);

        setFlag(SF, result < 0);

        boolean overflow =
                ((a ^ result)
                & (b ^ result)
                & 0x80000000) != 0;

        setFlag(OF, overflow);
    }

    /*
     * Atualiza as flags das operações:
     *
     * AND
     * OR
     * XOR
     */
    public void updateFlagsLogic(int result) {

        setFlag(CF, false);

        setFlag(OF, false);

        setFlag(ZF, result == 0);

        setFlag(SF, result < 0);
    }

    private void setFlag(int flag, boolean value) {

        if (value) {
            eflags |= (1 << flag);
        } else {
            eflags &= ~(1 << flag);
        }
    }

    @Override
    public String toString() {
        return "CPU32 {" +
                "\n\teax=" + eax +
                "\n\tebx=" + ebx +
                "\n\tecx=" + ecx +
                "\n\tedx=" + edx +
                "\n\tesi=" + esi +
                "\n\tedi=" + edi +
                "\n\tebp=" + ebp +
                "\n\tesp=" + esp +
                "\n\teip=" + eip +
                "\n\teflags=" + eflags +
                "\n}";
    }
}