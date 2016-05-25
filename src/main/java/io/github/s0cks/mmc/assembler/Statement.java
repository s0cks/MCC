package io.github.s0cks.mmc.assembler;

import io.github.s0cks.mmc.Address;
import io.github.s0cks.mmc.Binary;
import io.github.s0cks.mmc.Instruction;
import io.github.s0cks.mmc.Operand;
import io.github.s0cks.mmc.OperandAddress;
import io.github.s0cks.mmc.OperandInteger;
import io.github.s0cks.mmc.OperandRegister;

public final class Statement {
  public final String label;
  public final Instruction instruction;
  public final Operand<?>[] operands;

  public Statement(String label, Instruction instruction, Operand<?>... operands) {
    if (operands.length > 2) throw new IllegalStateException("Statements can only use no more than 2 operands");
    this.label = label;
    this.instruction = instruction;
    this.operands = operands;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder().append(this.label)
                                               .append(": ")
                                               .append(this.instruction)
                                               .append(' ')
                                               .append(this.operands[0]);

    if (this.operands.length > 1) {
      builder.append(' ')
             .append(this.operands[1]);
    }
    return builder.toString();
  }

  public void encode(Binary binary) {
    short a = 0;
    short b = 0;
    short op = 0;

    short extraBits[] = {
      -1, -1
    };

    switch (this.instruction) {
      case JMP:{
        a = this.encodeOperand(0, extraBits);
        binary.append(((short) (0x01C1 | (a << 10))));
        break;
      }
      case SYSCALL:{
        a = this.encodeOperand(0, extraBits);
        binary.append(((short) (0x01C2 | (a << 10))));
        break;
      }
      default: {
        op = (short) this.instruction.ordinal();
        a = this.encodeOperand(0, extraBits);
        b = this.encodeOperand(1, extraBits);

        binary.append(((short) (op | (a << 4) | (b << 10))));
      }
    }

    for(short sh : extraBits){
      if(sh != -1){
        binary.append(sh);
      }
    }
  }

  private short encodeOperand(int index, short[] extra) {
    Operand<?> o = this.operands[index];
    if (o != null) {
      switch (o.type()) {
        case REGISTER: {
          return (short) (((OperandRegister) o).value()
                                               .ordinal() & 7);
        }
        case LITERAL: {
          int value = ((OperandInteger) o).value();
          if(value < 0x20){
            return ((short) (value + 0x20));
          }
          extra[index] = ((short) value);
          return 0x1F;
        }
        case ADDRESS:{
          Address addr = ((OperandAddress) o).value();
          extra[index] = ((short) (addr.offset));
          return (short) (0x10 | (addr.register.ordinal() & 7));
        }
      }
    }

    return 0;
  }
}