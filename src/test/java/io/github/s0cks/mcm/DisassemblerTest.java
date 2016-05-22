package io.github.s0cks.mcm;

import io.github.s0cks.mcm.assembler.Parser;
import io.github.s0cks.mcm.assembler.Statement;
import io.github.s0cks.mcm.util.Disassembler;
import org.junit.Test;

import java.io.PrintWriter;
import java.util.List;

public class DisassemblerTest {
  
  @Test
  public void testDump()
  throws Exception {
    List<Statement> statements = new Parser(System.class.getResourceAsStream("/Test.mcm")).parse();
    Binary bin = new Binary();
    for(Statement statement : statements){
      statement.encode(bin);
    }

    Disassembler.dump(new PrintWriter(System.out), bin);
  }
}