package io.github.s0cks.mmc.ansi.parser;

import io.github.s0cks.mmc.ansi.type.CFunction;
import io.github.s0cks.mmc.ansi.type.CStruct;

import java.util.LinkedList;
import java.util.List;

public final class CodeUnit{
  private final List<CStruct> structs = new LinkedList<>();
  private final List<CFunction> functions = new LinkedList<>();

  public CStruct newStruct(String name){
    CStruct struct = new CStruct(name);
    this.structs.add(struct);
    return struct;
  }

  public CFunction newFunction(String name){
    CFunction func = new CFunction(name);
    this.functions.add(func);
    return func;
  }

  public CFunction getFunction(String name){
    for(CFunction func : this.functions){
      if(func.name.equals(name)){
        return func;
      }
    }

    return null;
  }
}