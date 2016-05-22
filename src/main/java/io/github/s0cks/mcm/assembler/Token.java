package io.github.s0cks.mcm.assembler;

public final class Token{
  public enum Kind{
    COMMENT,
    NAME,
    INT,
    LBRACKET,
    RBRACKET,
    COMMA,
    LABEL,
    PLUS,
    EOF;
  }

  public final Kind kind;
  public final String text;

  public Token(Kind kind, String text) {
    this.kind = kind;
    this.text = text;
  }
}