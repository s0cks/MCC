package io.github.s0cks.mmc;

import io.github.s0cks.mmc.assembler.Parser;
import io.github.s0cks.mmc.linker.Linker;
import io.github.s0cks.mmc.util.Disassembler;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

public class DisassemblerTest {
  
  @Test
  public void testDump()
  throws Exception {
    try{
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      (new Parser(System.class.getResourceAsStream("/HW.mmc"))).compile(bos);
      Linker.SimpleObjectFile hwSof = new Linker.SimpleObjectFile(new ByteArrayInputStream(bos.toByteArray()));

      FileOutputStream fos = new FileOutputStream(new File(new File(System.getProperty("user.home"), "Desktop"), "a.out"));
      (new Parser(System.class.getResourceAsStream("/HW.mmc"))).compile(new GZIPOutputStream(fos));

      Binary bin = (new Linker().link(System.class.getResourceAsStream("/Test.mmc"), hwSof));
      Disassembler.dump(new PrintWriter(System.out), bin);
    } catch(Exception e){
      throw new RuntimeException(e);
    }
  }
}