package io.github.s0cks.mmc;

public final class OperandAddress
implements Operand<Address>{
  public final Address address;

  public OperandAddress(Register base, int offset){
    this.address = new Address(base, offset);
  }

  @Override
  public Type type() {
    return Type.ADDRESS;
  }

  @Override
  public Address value() {
    return this.address;
  }
}