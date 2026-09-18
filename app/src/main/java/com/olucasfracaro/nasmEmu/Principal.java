package com.olucasfracaro.nasmEmu;

public class Principal
{
	public static void main(String[] args) {
	    String codigo = "\tnop"
        + "\ndec ebx"
        + "\nmov     eax, [ebx + 10]"
        + "\n\t  add ebx,eax  "
        + "\n\t  imul ecx,  eax, ebx "
        + "\n\t  sub     ebx,  5 ;comentario";
	    
		Lexer lexer = new Lexer(codigo);
		
		System.out.println(lexer);
	}
}
