package io.github.s0cks.mmc.ansi.ast;

public abstract class AstNode{
  protected final String name;

  protected AstNode(String name){
    this.name = name;
  }

  public abstract void visit(AstNodeVisitor visitor);
  public abstract void visitChildren(AstNodeVisitor visitor);
}