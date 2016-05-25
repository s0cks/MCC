package io.github.s0cks.mmc.ansi.parser;

import io.github.s0cks.mmc.ansi.ast.AstNode;
import io.github.s0cks.mmc.ansi.ast.BinaryOpNode;
import io.github.s0cks.mmc.ansi.ast.LiteralNode;
import io.github.s0cks.mmc.ansi.ast.ReturnNode;
import io.github.s0cks.mmc.ansi.ast.SequenceNode;
import io.github.s0cks.mmc.ansi.type.CFunction;
import io.github.s0cks.mmc.ansi.type.CInteger;
import io.github.s0cks.mmc.ansi.type.Type;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class Parser{
  private final Queue<Token> tokens;

  public static CodeUnit parse(InputStream in){
    return (new Parser(Lexer.lex(in)).parse());
  }

  public Parser(List<Token> tokens){
    this.tokens = ((LinkedList<Token>) tokens);
  }

  public CodeUnit parse(){
    CodeUnit unit = new CodeUnit();

    while(!this.tokens.isEmpty()){
      Token next = this.tokens.remove();

      switch(next.kind){
        case IDENTIFIER:{
          next = this.tokens.remove();
          this.parseFunctionDefinition(unit.newFunction(next.text));
          break;
        }
      }
    }

    return unit;
  }

  private AstNode parseUnaryExpr(){
    Token next;
    switch((next = this.tokens.remove()).kind){
      case NUMBER:{
        try{
          byte b = Byte.parseByte(next.text);
        } catch(Exception e){}

        try{
          int i = Integer.parseInt(next.text);
          return new LiteralNode(new CInteger(i));
        } catch(Exception e){}

        return new LiteralNode(new CInteger(0));
      }
      default: throw new IllegalStateException("unexpected: " + next.text);
    }
  }

  private AstNode parseBinaryExpr(){
    AstNode left = this.parseUnaryExpr();

    Token next;
    while(Token.Kind.isValidBinaryExprToken((next = this.tokens.remove()).kind)){
      left = new BinaryOpNode(Type.ID.BYTE, next.kind, left, this.parseBinaryExpr());
    }

    this.tokens.add(next);
    return left;
  }

  private void parseFunctionDefinition(CFunction func){
    Token next = this.tokens.remove();
    if(next.kind != Token.Kind.LPAREN){
      throw new IllegalStateException("unexpected: " + next.text);
    }

    while((next = this.tokens.remove()).kind != Token.Kind.RPAREN){
      if(next.kind == Token.Kind.RPAREN){
        break;
      }

      String param_name = next.text;
    }

    if((next = this.tokens.remove()).kind != Token.Kind.LBRACE){
      throw new IllegalStateException("unexpected: " + next.text);
    }

    SequenceNode code = new SequenceNode();

    while((next = this.tokens.remove()).kind != Token.Kind.RBRACE){
      switch(next.kind){
        case RETURN:{
          code.add(new ReturnNode(this.parseBinaryExpr()));
          if((next = this.tokens.remove()).kind != Token.Kind.SEMICOLON){
            throw new IllegalStateException("expected ';'");
          }
          break;
        }
        case RBRACE: break;
        default: throw new IllegalStateException("unexpected: " + next.text);
      }
    }

    func.setParsedCode(code);
  }
}