package io.github.s0cks.mcm;

public interface Operand<V>{
  enum Type{
    REGISTER,
    LITERAL;

    @Override
    public String toString() {
      return this.name().toLowerCase();
    }
  }

  public Type type();
  public V value();
}