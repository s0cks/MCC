package io.github.s0cks.mcm;

public final class OperandInteger
implements Operand<Integer>{
  private final int value;

  public OperandInteger(int value){
    this.value = value;
  }

  @Override
  public Type type() {
    return Type.LITERAL;
  }

  @Override
  public Integer value() {
    return this.value;
  }

  @Override
  public String toString() {
    return String.valueOf(this.value);
  }
}
