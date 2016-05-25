package io.github.s0cks.mmc.ansi.ast;

import io.github.s0cks.mmc.ansi.type.Type;

public final class LiteralNode
extends AstNode{
  public final Type literal;

  public LiteralNode(Type literal){
    super("Literal");
    this.literal = literal;
  }

  @Override
  public void visit(AstNodeVisitor visitor) {
    visitor.visitLiteralNode(this);
  }

  @Override
  public void visitChildren(AstNodeVisitor visitor) {
    // Fallthrough
  }
}