package io.github.s0cks.mcm.ansi.parser;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import io.github.s0cks.mcm.Binary;
import io.github.s0cks.mcm.ansi.compiler.ANSICCompiler;
import io.github.s0cks.mcm.assembler.Statement;
import io.github.s0cks.mcm.util.Disassembler;
import org.junit.Test;

import java.io.PrintWriter;

public class ParserTest {
  @Test
  public void testParse()
  throws Exception {
    try(Lexer lexer = new Lexer(System.class.getResourceAsStream("/Test.c"))){
      Parser parser = new Parser(lexer.lex());
      CodeUnit code = parser.parse();

      try(ByteOutputStream bos = new ByteOutputStream();
          ANSICCompiler compiler = new ANSICCompiler(code.getFunction("main"), bos)) {
        code.getFunction("main")
            .getParsedCode()
            .visit(compiler);

        try (ByteInputStream bis = new ByteInputStream(bos.getBytes(), bos.getCount())){
          io.github.s0cks.mcm.assembler.Parser assm = new io.github.s0cks.mcm.assembler.Parser(bis);
          Binary bin = new Binary();
          for(Statement statement : assm.parse()){
            statement.encode(bin);
          }

          Disassembler.dump(new PrintWriter(System.out), bin);
        }
      }
    }
  }
}