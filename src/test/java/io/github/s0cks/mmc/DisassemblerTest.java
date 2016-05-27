package io.github.s0cks.mmc;

import io.github.s0cks.mmc.assembler.Parser;
import io.github.s0cks.mmc.util.Disassembler;
import org.junit.Test;

import java.io.PrintWriter;

public class DisassemblerTest {
  
  @Test
  public void testDump()
  throws Exception {
    Binary bin = (new Parser(System.class.getResourceAsStream("/Test.mmc"))).compile();
    System.out.println("Dumping");
    Disassembler.dump(new PrintWriter(System.out), bin);
  }
}