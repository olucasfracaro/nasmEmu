package com.olucasfracaro.nasmEmu;

import java.util.Set;

public class Analisador {

    private static final Set<String> REGISTRADORES = Set.of(
            //32-bit registros gerais
            "eax", "ebx", "ecx", "edx", "esi", "edi", "esp", "ebp",
            //16-bit
            "ax", "bx", "cx", "dx", "si", "di", "sp", "bp",
            //8-bit
            "al", "ah", "bl", "bh", "cl", "ch", "dl", "dh",
            //segmentos e controle
            "cs", "ds", "ss", "es", "fs", "gs", "eflags", "eip"
            // TODO (64-bit): "rax", "rbx", "rcx", "rdx", "rsi", "rdi", "rsp", "rbp", "r8"-"r15", "r8b"-"r15b", etc.
    );

    private static final Set<String> INSTRUCOES = Set.of(
            //movimentação e pilha
            "mov", "movzx", "movsx", "lea", "xchg", "xadd", "cmpxchg",
            "push", "pop", "pusha", "popa", "pushad", "popad", "pushf", "popf", "pushfd", "popfd",

            //aritmética e lógica
            "add", "adc", "sub", "sbb", "mul", "imul", "div", "idiv",
            "inc", "dec", "neg", "cmp", "and", "or", "xor", "not", "test",

            //deslocamento e rotação
            "shl", "shr", "sal", "sar", "rol", "ror", "rcl", "rcr",

            //controle de fluxo
            "jmp", "call", "ret", "leave", "enter",
            "je", "jne", "jz", "jnz", "js", "jns", "jo", "jno", "jc", "jnc",
            "jb", "jnae", "jbae", "jae", "ja", "jbe", "jl", "jnge", "jge", "jnl",
            "jle", "jng", "jg", "jnle", "jpe", "jpo", "loop", "loope", "loopne",
            "loopz", "loopnz", "jecxz",

            //operações em cadeia (strings) e bits
            "bsf", "bsr", "bt", "bts", "btr", "btc",
            "movsb", "movsw", "movsd", "cmpsb", "cmpsw", "cmpsd",
            "scasb", "scasw", "scasd", "lodsb", "lodsw", "lodsd",
            "stosb", "stosw", "stosd", "rep", "repe", "repne", "repz", "repnz",

            //conversão e extensão de sinal
            "cbw", "cwde", "cwd", "cdq",

            //flags e sistema
            "clc", "stc", "cmc", "cld", "std", "cli", "sti", "lahf", "sahf",
            "nop", "int", "into", "iret", "iretd", "hlt", "cpuid", "rdtsc"
            // TODO (64-bit): "movabs", "syscall", "sysret", "cqo", "cdqe", "cmpxchg16b", etc.
    );

    private static final Set<String> DIRETIVAS = Set.of(
            "bits", "global", "extern", "section", "segment",
            "db", "dw", "dd", "dq", "dt", "do", "dy", "dz",
            "resb", "resw", "resd", "resq", "rest", "reso", "resy", "resz",
            "equ", "times", "define", "include", "macro", "endmacro"
    );

    private static final Set<String> OPERADORES = Set.of(
            "+", "-", "*", "/", "%", "&", "|", "^", "~", "<<", ">>"
    );

    public static boolean isInstrucao(String palavra) {
        return INSTRUCOES.contains(palavra.toLowerCase());
    }

    public static boolean isRegistrador(String palavra) {
        return REGISTRADORES.contains(palavra.toLowerCase());
    }

    public static boolean isDiretiva(String palavra) {
        return DIRETIVAS.contains(palavra.toLowerCase());
    }

    public static boolean isOperador(String palavra) {
        return OPERADORES.contains(palavra);
    }

    public static boolean isTexto(String palavra) {
        return palavra.startsWith("\"") && palavra.endsWith("\"");
    }

    public static boolean isNumero(String palavra) {
        return palavra.matches("-?(\\d+|0[xX][0-9a-fA-F]+)");
    }
}