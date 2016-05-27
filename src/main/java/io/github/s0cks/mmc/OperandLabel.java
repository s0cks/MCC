package io.github.s0cks.mmc;

public final class OperandLabel
implements Operand<Integer>{
  private final int address;

  public OperandLabel(int address){
    this.address = address;
  }

  @Override
  public Type type() {
    return Type.LABEL;
  }

  @Override
  public Integer value() {
    return this.address;
  }
}