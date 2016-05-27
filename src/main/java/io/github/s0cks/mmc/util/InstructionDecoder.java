package io.github.s0cks.mmc.util;

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

  public static boolean isJmp(short code){
    return ((code & 0x03FF) == 0x1C1);
  }

  public static boolean isSysCall(short code){
    return ((code & 0x03FF) == 0x1C2);
  }

  public static boolean isCall(short code){
    return ((code & 0x03FF) == 0x1C3);
  }
}