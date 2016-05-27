package io.github.s0cks.mmc.assembler;

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
    EOF,
    BYTES;
  }

  public final Kind kind;
  public final String text;

  public Token(Kind kind, String text) {
    this.kind = kind;
    this.text = text;
  }
}