package io.github.s0cks.mmc;

public final class Address {
  public final Register register;
  public final int offset;

  public Address(Register register, int offset) {
    this.register = register;
    this.offset = offset;
  }
}
