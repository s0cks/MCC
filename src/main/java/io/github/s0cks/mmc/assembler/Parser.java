package io.github.s0cks.mmc.assembler;

import io.github.s0cks.mmc.Binary;
import io.github.s0cks.mmc.Instruction;
import io.github.s0cks.mmc.Operand;
import io.github.s0cks.mmc.OperandAddress;
import io.github.s0cks.mmc.OperandBytes;
import io.github.s0cks.mmc.OperandInteger;
import io.github.s0cks.mmc.OperandLabel;
import io.github.s0cks.mmc.OperandRegister;
import io.github.s0cks.mmc.Register;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public final class Parser{
  private final Queue<Token> tokens;
  private final Map<String, Short> labels = new HashMap<>();
  private final Map<Integer, String> fixups = new HashMap<>();

  public Parser(InputStream in){
    this.tokens = ((LinkedList<Token>) Lexer.lex(in));
  }

  public Binary compile(){
    Binary bin = new Binary();

    Instruction instr;
    String label = null;
    Operand<?>[] operands = new Operand[2];
    Token next;
    while(!this.tokens.isEmpty()){
      next = this.tokens.remove();

      switch(next.kind){
        case COMMENT: continue;
        case LABEL:{
          int addr = bin.counter();
          this.labels.put(next.text, (short) addr);
          label = next.text;
          continue;
        }
        case NAME:{
          instr = Instruction.valueOf(next.text.toUpperCase());

          if(instr == Instruction.RET){
            break;
          }

          operands[0] = this.nextOperand(bin);
          if(this.tokens.peek() != null && this.tokens.peek().kind == Token.Kind.COMMA){
            this.tokens.remove();
            operands[1] = this.nextOperand(bin);
          }
          break;
        }
        default: throw new IllegalStateException("Unexpected " + next.kind + ": " + next.text);
      }

      Statement statement = new Statement(label, instr, operands);
      statement.encode(bin);
    }

    for(Map.Entry<Integer, String> fixup : this.fixups.entrySet()){
      bin.setAt(fixup.getKey() + 1, this.labels.get(fixup.getValue()));
    }

    return bin;
  }

  private Operand<?> nextOperand(Binary bin){
    Token next = this.tokens.remove();
    try{
      switch(next.kind){
        case BYTES:{
          return new OperandBytes(next.text);
        }
        case NAME:{
          try{
            return new OperandRegister(Register.valueOf(next.text.toUpperCase()));
          } catch(Exception e) {
            // Not a Register
          }

          if(!this.labels.containsKey(next.text)){
            this.fixups.put(bin.counter(), next.text);
            return new OperandLabel(0);
          }

          return new OperandLabel(this.labels.get(next.text));
        }
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
      throw new IllegalStateException(e);
    }
  }
}