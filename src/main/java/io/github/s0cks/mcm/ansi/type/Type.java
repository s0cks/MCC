package io.github.s0cks.mcm.ansi.type;

public abstract class Type{
  public enum ID{
    STRUCT,
    INTEGER,
    SHORT,
    LONG,
    BYTE,
    DOUBLE,
    FLOAT,
    FUNCTION;
  }

  public final ID type;

  protected Type(ID type){
    this.type = type;
  }

  public boolean is(ID type){
    return this.type.equals(type);
  }
}