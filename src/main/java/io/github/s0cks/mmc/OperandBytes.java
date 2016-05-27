package io.github.s0cks.mmc;

public final class OperandBytes
implements Operand<short[]>{
  private final short[] bytes;

  public OperandBytes(String str){
    bytes = new short[(str.length() * 2)];
    for(int i = 0, j = 0; i < bytes.length - 1; j++, i += 2){
      char c = str.charAt(j);
      bytes[i] = ((short) (c & 0xFF));
      bytes[i + 1] = ((short) ((c >> 8) & 0xFF));
    }
  }

  @Override
  public Type type() {
    return Type.BYTES;
  }

  @Override
  public short[] value() {
    return this.bytes;
  }
}