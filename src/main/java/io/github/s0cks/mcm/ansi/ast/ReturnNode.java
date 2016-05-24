package io.github.s0cks.mcm.ansi.ast;

public final class ReturnNode
extends AstNode{
  public final AstNode value;

  public ReturnNode(AstNode value){
    super("Return");
    this.value = value;
  }

  @Override
  public void visit(AstNodeVisitor visitor) {
    visitor.visitReturnNode(this);
  }

  @Override
  public void visitChildren(AstNodeVisitor visitor) {
    this.value.visit(visitor);
  }
}