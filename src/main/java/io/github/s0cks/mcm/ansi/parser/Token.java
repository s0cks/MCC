package io.github.s0cks.mcm.ansi.parser;

public final class Token{
  public enum Kind{
    // TYPES
    TRUE,
    FALSE,
    NULL,
    VOID,
    CHAR,
    NUMBER,
    // Keywords
    STRUCT,
    PRIVATE,
    STATIC,
    RETURN,
    // Symbols
    ADD,
    SUB,
    DIV,
    MUL,
    LBRACE,
    RBRACE,
    LBRACKET,
    RBRACKET,
    LPAREN,
    RPAREN,
    SEMICOLON,
    DOT,
    // Misc
    IDENTIFIER,
    EOF;

    public static boolean isValidBinaryExprToken(Kind kind){
      switch(kind){
        case ADD:
        case SUB:
        case DIV:
        case MUL:
          return true;
        default:
          return false;
      }
    }
  }

  public final String text;
  public final Kind kind;

  public Token(String text, Kind kind) {
    this.text = text;
    this.kind = kind;
  }
}