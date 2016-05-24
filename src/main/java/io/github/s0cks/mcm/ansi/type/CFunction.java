package io.github.s0cks.mcm.ansi.type;

import io.github.s0cks.mcm.ansi.ast.SequenceNode;

public final class CFunction
extends Type{
  public final String name;
  private SequenceNode parsedCode;

  public CFunction(String name){
    super(ID.FUNCTION);
    this.name = name;
  }

  public void setParsedCode(SequenceNode code){
    this.parsedCode = code;
  }

  public SequenceNode getParsedCode(){
    return this.parsedCode;
  }
}