package io.github.s0cks.mcm.assembler;

import io.github.s0cks.mcm.Binary;
import io.github.s0cks.mcm.Instruction;
import io.github.s0cks.mcm.Operand;
import io.github.s0cks.mcm.OperandInteger;
import io.github.s0cks.mcm.OperandRegister;

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

    switch (this.instruction) {
      default: {
        a = this.encodeOperand(0, binary);
        b = this.encodeOperand(1, binary);
      }
    }

    short op = (short) this.instruction.ordinal();
    binary.append(((short) (op | (a << 4) | (b << 10))));
  }

  private short encodeOperand(int index, Binary binary) {
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
            return (short) (value + 0x20);
          }
          binary.append(((short) value));
          return 0x1F;
        }
      }
    }

    return 0;
  }
}