package io.github.s0cks.mmc;

public interface Operand<V>{
  enum Type{
    REGISTER,
    ADDRESS,
    LITERAL,
    LABEL,
    BYTES;

    @Override
    public String toString() {
      return this.name().toLowerCase();
    }
  }

  public Type type();
  public V value();
}