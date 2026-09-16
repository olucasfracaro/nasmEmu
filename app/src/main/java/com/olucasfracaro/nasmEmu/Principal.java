package com.olucasfracaro.nasmEmu;

public class Principal {
    public static void main(String[] args) {
        CPU32 cpu = new CPU32();

        // EAX = 30
        Instruction addEax = new Add(
            cpu.eax(),
            new Immediate32(30)
        );

        // EBX = 20
        Instruction addEbx = new Add(
            cpu.ebx(),
            new Immediate32(15)
        );

        addEax.execute(cpu);
        addEbx.execute(cpu);

        // ECX = EAX
        Instruction movEcx = new Mov(
            cpu.ecx(),
            cpu.eax()
        );

        // ECX = ECX + EBX
        Instruction addEcx = new Add(
            cpu.ecx(),
            cpu.ebx()
        );

        movEcx.execute(cpu);
        addEcx.execute(cpu);

		Instruction xorEax = new Xor(
			cpu.eax(),
			cpu.eax()
		);

        Instruction andEbx = new And(
            cpu.ebx(),
            new Immediate32(1)
        );

        xorEax.execute(cpu);
        andEbx.execute(cpu);

        System.out.println(cpu);
    }
}
