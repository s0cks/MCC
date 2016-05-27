package io.github.s0cks.mmc.util;

import io.github.s0cks.mmc.Binary;
import io.github.s0cks.mmc.Instruction;
import io.github.s0cks.mmc.Register;

import java.io.PrintWriter;

public final class Disassembler {
  private Disassembler() {}

  private static int disassembleOperand(PrintWriter writer, short value, Binary binary, int counter) {
    if (value < Register.values().length) {
      writer.write(Register.values()[value & 0x7].toString());
      return 0;
    } else if(value == 0x3F){
      int len = binary.get(counter++);

      String str = "";
      for(int i = counter; i < counter + len - 1; i += 2){
        str += ((char) (binary.get(i) | (binary.get(i + 1) << 8)));
      }
      writer.write(str);
      System.out.println("<<<");
      return len + 1;
    } else if (value < 0x10 + Register.values().length) {
      Register reg = Register.values()[value & 0x7];
      int offset = ((int) binary.get(counter));

      writer.write(String.format(
      "[%s + 0x%s]",
      reg,
      Integer.toString(offset, 16)
             .toUpperCase()
      ));
    } else if(value == 0x2F){
      System.out.println(">>>");

      int label = (binary.get(counter) | (binary.get(counter + 1) << 8));
      writer.write(Integer.toString(label));
      return 2;
    } else if(value == 0x1F){
      System.out.println("...");
      writer.write(Integer.toString(binary.get(counter)));
      return 1;
    }
    return 1;
  }

  public static void dump(PrintWriter writer, Binary binary) {
    int counter = 0;
    while (counter < binary.counter()) {
      short op = InstructionDecoder.decodeInstruction(binary.get(counter));
      short a = InstructionDecoder.decodeOperandA(binary.get(counter));
      short b = InstructionDecoder.decodeOperandB(binary.get(counter));

      if (op > 0) {
        Instruction instr;
        if (InstructionDecoder.isJmp(binary.get(counter))) {
          instr = Instruction.JMP;

          writer.write(instr.toString());
          writer.write(" ");
          counter += disassembleOperand(writer, b, binary, counter + 1);
          writer.write("\n");
        } else if (InstructionDecoder.isSysCall(binary.get(counter))) {
          instr = Instruction.SYSCALL;

          writer.write(instr.toString());
          writer.write(" ");
          counter += disassembleOperand(writer, b, binary, counter + 1);
          writer.write("\n");
        } else {
          instr = Instruction.values()[op];

          writer.write(instr.toString());
          if (instr == Instruction.RET) {
            writer.write("\n");
            counter += 1;
            continue;
          }

          writer.write(" ");
          counter += disassembleOperand(writer, a, binary, counter + 1);

          if (instr == Instruction.POP || instr == Instruction.PUSH || instr == Instruction.CALL || instr == Instruction.DB) {
            writer.write("\n");
            counter += 1;
            continue;
          }

          writer.write(", ");
          counter += disassembleOperand(writer, b, binary, counter + 1);
          writer.write('\n');
        }
      } else {
        writer.write("<unknown opcode: ");
        writer.write(Integer.toString(op, 16));
        writer.write(">\n");
      }

      counter += 1;
    }

    writer.flush();
  }
}