package io.github.s0cks.mcm.assembler;

import io.github.s0cks.mcm.Instruction;
import io.github.s0cks.mcm.Operand;
import io.github.s0cks.mcm.OperandAddress;
import io.github.s0cks.mcm.OperandInteger;
import io.github.s0cks.mcm.OperandRegister;
import io.github.s0cks.mcm.Register;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class Parser{
  private final Queue<Token> tokens;

  public Parser(InputStream in){
    this.tokens = ((LinkedList<Token>) Lexer.lex(in));
  }

  public List<Statement> parse(){
    List<Statement> statements = new LinkedList<>();

    String label = null;
    Instruction instr;
    Operand<?>[] operands = new Operand[2];

    Token next;
    while(!this.tokens.isEmpty()){
      next = this.tokens.remove();
      switch(next.kind){
        case COMMENT: continue;
        case LABEL:{
          label = next.text;
          continue;
        }
        case NAME:{
          instr = Instruction.valueOf(next.text.toUpperCase());

          if(instr == Instruction.RET){
            break;
          }

          operands[0] = this.nextOperand();
          if(this.tokens.peek() != null && this.tokens.peek().kind == Token.Kind.COMMA){
            this.tokens.remove();
            operands[1] = this.nextOperand();
          }
          break;
        }
        default:{
          throw new IllegalStateException("unexpected token: " + next.text);
        }
      }

      Statement statement = new Statement(label, instr, operands);
      statements.add(statement);
      operands = new Operand[2];
    }

    return statements;
  }

  private Operand<?> nextOperand(){
    Token next = this.tokens.remove();
    try{
      switch(next.kind){
        case NAME: return new OperandRegister(Register.valueOf(next.text.toUpperCase()));
        case INT: return new OperandInteger(Integer.valueOf(next.text));
        case LBRACKET:{
          if((next = this.tokens.remove()).kind != Token.Kind.NAME){
            throw new IllegalStateException(String.format("%s not a register", next.text));
          }

          Register reg = Register.valueOf(next.text);

          if((next = this.tokens.remove()).kind != Token.Kind.PLUS){
            throw new IllegalStateException(String.format("'%s' illegal address mode", next.text));
          }

          Integer offset = Integer.valueOf((next = this.tokens.remove()).text);
          System.out.println(next.text);

          if((next = this.tokens.remove()).kind != Token.Kind.RBRACKET){
            throw new IllegalStateException("Address not closed: " + next.text);
          }

          return new OperandAddress(reg, offset);
        }
        default:{
          throw new IllegalStateException("unexpected operand: " + next.text);
        }
      }
    } catch(Exception e){
      throw new IllegalStateException("Lookahead: " + this.tokens.remove().text, e);
    }
  }
}