package io.github.s0cks.mcm.util;

public final class InstructionDecoder{
  private InstructionDecoder(){}

  public static short decodeInstruction(short code){
    return (short) (code & 0xF);
  }

  public static short decodeOperandA(short code){
    return (short) ((code >> 4) & 0x3F);
  }

  public static short decodeOperandB(short code){
    return (short) ((code >> 10) & 0x3F);
  }
}