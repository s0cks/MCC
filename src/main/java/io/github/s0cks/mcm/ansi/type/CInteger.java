package io.github.s0cks.mcm.ansi.type;

public final class CInteger
extends Type{
  public final int value;

  public CInteger(int value){
    super(ID.INTEGER);
    this.value = value;
  }
}