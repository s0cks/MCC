package io.github.s0cks.mmc.ansi.parser;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Lexer
implements Closeable{
  private static final Map<String, Token.Kind> keywords = new HashMap<>();
  private static final Set<Character> symbols = new HashSet<>();

  private static void addKeyword(String kw, Token.Kind kind){
    keywords.put(kw, kind);
  }

  private static void addSymbol(char c){
    symbols.add(c);
  }

  public static boolean isSymbol(char c){
    return symbols.contains(c);
  }

  public static boolean isKeyword(String kw){
    return keywords.containsKey(kw);
  }

  static{
    addKeyword("struct", Token.Kind.STRUCT);
    addKeyword("private", Token.Kind.PRIVATE);
    addKeyword("static", Token.Kind.STATIC);
    addKeyword("return", Token.Kind.RETURN);

    addSymbol('/');
    addSymbol('+');
    addSymbol('*');
    addSymbol('-');
    addSymbol('{');
    addSymbol('}');
    addSymbol('(');
    addSymbol(')');
    addSymbol(';');
  }

  public static List<Token> lex(InputStream in){
    try(Lexer lexer = new Lexer(in)){
      return lexer.lex();
    } catch(Exception e){
      throw new RuntimeException(e);
    }
  }

  private final InputStream in;
  private char peek = '\0';

  public Lexer(InputStream in){
    if(in == null) throw new NullPointerException("in == null");
    this.in = in;
  }

  public List<Token> lex()
  throws IOException{
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
    switch(next){
      case '/': return new Token("/", Token.Kind.DIV);
      case '+': return new Token("+", Token.Kind.ADD);
      case '*': return new Token("*", Token.Kind.MUL);
      case '-': return new Token("-", Token.Kind.SUB);
      case '{': return new Token("{", Token.Kind.LBRACE);
      case '}': return new Token("}", Token.Kind.RBRACE);
      case '(': return new Token("(", Token.Kind.LPAREN);
      case ')': return new Token(")", Token.Kind.RPAREN);
      case ';': return new Token(";", Token.Kind.SEMICOLON);
      case '\0': return new Token("", Token.Kind.EOF);
      default: break;
    }

    String buffer = "";
    if(next == '\''){
      char c = this.next();
      if(this.next() != '\''){
        throw new IllegalStateException("unclosed character quote");
      }
      return new Token("" + c, Token.Kind.CHAR);
    } else if(Character.isDigit(next)){
      buffer += next;

      boolean dbl = false;
      while((Character.isDigit(next = this.peek()) || (next == '.' && !dbl))){
        buffer += next;
        if(next == '.') dbl = true;
        this.next();
      }

      return new Token(buffer, Token.Kind.NUMBER);
    } else{
      buffer += next;
      while(!Character.isWhitespace(next = this.peek()) && !isSymbol(next)){
        buffer += next;
        this.next();
        if(isKeyword(buffer)){
          return new Token(buffer, keywords.get(buffer));
        }
      }

      return new Token(buffer, Token.Kind.IDENTIFIER);
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

  @Override
  public void close()
  throws IOException {
    this.in.close();
  }
}