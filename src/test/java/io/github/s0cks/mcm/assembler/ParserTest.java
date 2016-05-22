package io.github.s0cks.mcm.assembler;

import org.junit.Test;

import java.util.List;

public class ParserTest {
  
  @Test
  public void testParse()
  throws Exception {
    Parser parser = new Parser(System.class.getResourceAsStream("/Test.mcm"));
    List<Statement> statements = parser.parse();
    for(Statement statement : statements){
      System.out.println(statement);
    }
  }
}