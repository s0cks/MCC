package io.github.s0cks.mmc.ansi.ast;

import io.github.s0cks.mmc.ansi.parser.Token;
import io.github.s0cks.mmc.ansi.type.Type;

public final class BinaryOpNode
extends AstNode{
  public final Type.ID type;
  public final AstNode left;
  public final AstNode right;
  public final Token.Kind kind;

  public BinaryOpNode(Type.ID type, Token.Kind kind, AstNode left, AstNode right){
    super("BinaryOp");
    this.type = type;
    this.kind = kind;
    this.left = left;
    this.right = right;
  }

  @Override
  public void visit(AstNodeVisitor visitor) {
    visitor.visitBinaryOpNode(this);
  }

  @Override
  public void visitChildren(AstNodeVisitor visitor) {
    this.left.visit(visitor);
    this.right.visit(visitor);
  }
}