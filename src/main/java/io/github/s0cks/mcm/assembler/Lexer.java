package io.github.s0cks.mcm.assembler;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class Lexer
implements Closeable {
  private static final Set<Character> validSymbols = new HashSet<>();

  private static void addSymbol(char c){
    validSymbols.add(c);
  }

  static{
    addSymbol('_');
    addSymbol('+');
    addSymbol(']');
    addSymbol('[');
  }

  public static boolean isSymbol(char c){
    return validSymbols.contains(c);
  }

  private final InputStream in;
  private char peek = '\0';

  public Lexer(InputStream in){
    if(in == null) throw new NullPointerException("input == null");
    this.in = in;
  }

  public static List<Token> lex(InputStream in){
    try(Lexer lexer = new Lexer(in)){
      return lexer.lex();
    } catch(Exception e){
      throw new RuntimeException(e);
    }
  }

  public List<Token> lex()
  throws Exception{
    List<Token> tokens = new LinkedList<>();
    Token next;
    while((next = this.nextToken()).kind != Token.Kind.EOF){
      tokens.add(next);
    }
    return tokens;
  }

  private Token nextToken()
  throws IOException{
    char next = this.nextReal();
    String buffer = "";
    switch(next){
      case ';':{
        next = this.nextReal();
        do{
          buffer += next;
        } while((next = this.next()) != '\n');
        return new Token(Token.Kind.COMMENT, buffer);
      }
      case '[': return new Token(Token.Kind.LBRACKET, "[");
      case ']': return new Token(Token.Kind.RBRACKET, "]");
      case '+': return new Token(Token.Kind.PLUS, "+");
      case ',': return new Token(Token.Kind.COMMA, ",");
      default: break;
    }

    if(Character.isAlphabetic(next) || isSymbol(next)){
      buffer += next;
      while(Character.isAlphabetic(next = this.peek()) || Character.isDigit(next) || isSymbol(next)){
        buffer += next;
        this.next();
      }

      if(this.peek() == ':'){
        this.next();
        return new Token(Token.Kind.LABEL, buffer);
      } else{
        return new Token(Token.Kind.NAME, buffer);
      }
    } else if(Character.isDigit(next)){
      buffer += next;
      while((Character.isDigit(next = this.peek()))){
        buffer += next;
        this.next();
      }
      return new Token(Token.Kind.INT, buffer);
    } else if(next == '\0'){
      return new Token(Token.Kind.EOF, "");
    } else{
      throw new IllegalStateException("undefined: " + next);
    }
  }

  private char nextReal()
  throws IOException{
    char next;
    while(Character.isWhitespace(next = this.next()));
    return next;
  }

  private char next()
  throws IOException{
    int ret;
    if(this.peek == '\0'){
      ret = this.in.read();
    } else{
      ret = this.peek;
      this.peek = '\0';
    }

    if(ret == -1) return '\0';
    return (char) ret;
  }

  private char peek()
  throws IOException{
    if(this.peek != '\0'){
      return this.peek;
    }

    int peek = this.in.read();
    if(peek == -1){
      return '\0';
    } else{
      this.peek = (char) peek;
    }

    return this.peek;
  }

  public void close()
  throws IOException {
    this.in.close();
  }
}