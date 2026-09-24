# nasmEmu

Emulador de uma CPU x86 de 32 bits desenvolvido em Java. O projeto está em
desenvolvimento e, neste momento, concentra-se na leitura de código Assembly
no estilo NASM e na construção da base do emulador.

## Estado atual

- leitura e tokenização de código NASM;
- reconhecimento de instruções, diretivas, labels, números, strings,
  operadores e comentários;
- suporte a números decimais, hexadecimais e acesso à memória;
- estrutura inicial da CPU e de algumas operações básicas.

O fluxo executado atualmente lê um arquivo de exemplo e imprime os tokens
gerados pelo lexer para fins de teste. **A execução completa de programas Assembly ainda não está
implementada.**

## Requisitos

- JDK 21 ou superior;
- Linux, macOS ou Windows;
- Gradle Wrapper incluído no repositório.

## Como executar

Na raiz do projeto, execute:

```bash
./gradlew :app:run
```

No Windows:

```powershell
./gradlew.bat :app:run
```

O programa exibirá no terminal os tokens identificados no arquivo de teste.

## Desenvolvimento

Para compilar o projeto sem executá-lo:

```bash
./gradlew :app:build
```

Os testes automatizados podem ser executados com:

```bash
./gradlew :app:test
```

## Próximos passos

- separar análise léxica, parsing e execução;
- implementar memória e o ciclo de busca, decodificação e execução;
- adicionar suporte à execução das instruções reconhecidas pelo lexer;
- ampliar a cobertura de testes;
- criar uma CPU 64 bits.
