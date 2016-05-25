package io.github.s0cks.mmc.ansi.type;

import java.util.LinkedList;
import java.util.List;

public final class CStruct
extends Type{
  public static final class Field<V>{
    public final String name;
    public V value;

    public Field(String name, V value) {
      this.name = name;
      this.value = value;
    }

    public Field(String name){
      this(name, null);
    }

    public void set(V value){
      this.value = value;
    }
  }

  private final List<Field<?>> fields = new LinkedList<>();
  public final String name;

  public CStruct(String name){
    super(ID.STRUCT);
    this.name = name;
  }

  public <T> Field<T> createField(String name){
    Field<T> field = new Field<>(name);
    this.fields.add(field);
    return field;
  }
}