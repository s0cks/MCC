package io.github.s0cks.mcm.util;

import io.github.s0cks.mcm.Binary;
import io.github.s0cks.mcm.Instruction;
import io.github.s0cks.mcm.Register;

import java.io.PrintWriter;

public final class Disassembler {
  private Disassembler() {}

  private static int disassembleOperand(PrintWriter writer, short value, Binary binary, int counter) {
    if (value < Register.values().length) {
      writer.write(Register.values()[value & 7].toString());
      return 0;
    } else if (value < 0x10 + Register.values().length) {
      Register reg = Register.values()[value & 0x7];
      int offset = ((int) binary.get(counter));

      writer.write(String.format(
        "[%s + 0x%s]",
        reg,
        Integer.toString(offset, 16).toUpperCase()
      ));
      return 1;
    } else if (value >= 0x20) {
      String hex = Integer.toString(value - 0x20, 16).toUpperCase();
      writer.write("0x" + hex);
      return 0;
    } else{
      if(value == 0){
        writer.write("0");
      }
    }
    return 0;
  }

  public static void dump(PrintWriter writer, Binary binary) {
    int counter = 0;
    while (counter < binary.counter()) {
      short op = InstructionDecoder.decodeInstruction(binary.get(counter));
      short a = InstructionDecoder.decodeOperandA(binary.get(counter));
      short b = InstructionDecoder.decodeOperandB(binary.get(counter));
      counter++;

      if (op >= 0) {
        Instruction instr = Instruction.values()[op];
        writer.write(instr.toString());
        if(instr == Instruction.RET){
          writer.write("\n");
          continue;
        }

        writer.write(" ");
        counter += disassembleOperand(writer, a, binary, counter);

        if(instr == Instruction.POP || instr == Instruction.PUSH){
          writer.write("\n");
          continue;
        }

        writer.write(", ");
        counter += disassembleOperand(writer, b, binary, counter);
        writer.write('\n');
      } else {
        writer.write("<unknown opcode: ");
        writer.write(Integer.toString(op, 16));
        writer.write(">\n");
      }
    }

    writer.flush();
  }
}