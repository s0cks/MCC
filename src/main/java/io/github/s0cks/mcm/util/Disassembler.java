package io.github.s0cks.mcm.util;

import io.github.s0cks.mcm.Binary;
import io.github.s0cks.mcm.Instruction;
import io.github.s0cks.mcm.Register;

import java.io.PrintWriter;

public final class Disassembler{
  private Disassembler(){}

  private static int disassembleOperand(PrintWriter writer, short value){
    if(value < Register.values().length){
      writer.write(Register.values()[value & 7].toString());
      return 0;
    } else if(value > 0x20){
      writer.write(Integer.toString(value - 0x20));
      return 0;
    } else{
      switch(value){
        case 0x1F:{
          writer.write("0x" + Integer.toString(value, 16));
          break;
        }
      }
      return 1;
    }
  }

  public static void dump(PrintWriter writer, Binary binary){
    int counter = 0;
    while(counter < binary.counter()){
      short op = InstructionDecoder.decodeInstruction(binary.get(counter));
      short a = InstructionDecoder.decodeOperandA(binary.get(counter));
      short b = InstructionDecoder.decodeOperandB(binary.get(counter));
      counter++;

      if(op >= 0){
        writer.write(Instruction.values()[op].toString());
        writer.write(" ");
        counter += disassembleOperand(writer, a);
        writer.write(", ");
        counter += disassembleOperand(writer, b);
        writer.write('\n');
      } else{
        writer.write("<unknown opcode: ");
        writer.write(Integer.toString(op, 16));
        writer.write(">\n");
      }
    }

    writer.flush();
  }
}