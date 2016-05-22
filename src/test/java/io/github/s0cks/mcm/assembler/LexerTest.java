package io.github.s0cks.mcm.assembler;

import org.junit.Test;

import java.util.List;

public class LexerTest {
  @Test
  public void testLex()
  throws Exception {
    List<Token> tokens = Lexer.lex(System.class.getResourceAsStream("/Test.mcm"));
    for(Token token : tokens){
      System.out.println(token.kind + ": " + token.text);
    }
  }
}