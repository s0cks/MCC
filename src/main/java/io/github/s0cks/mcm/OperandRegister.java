package io.github.s0cks.mcm;

public final class OperandRegister
implements Operand<Register>{
  private final Register value;

  public OperandRegister(Register value){
    this.value = value;
  }

  public Type type() {
    return Type.REGISTER;
  }

  public Register value() {
    return this.value;
  }

  @Override
  public String toString() {
    return this.value.toString();
  }
}