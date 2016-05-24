package io.github.s0cks.mcm.ansi.ast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class SequenceNode
extends AstNode
implements Iterable<AstNode>{
  private final List<AstNode> nodes = new LinkedList<>();

  public SequenceNode(){
    super("Sequence");
  }

  public SequenceNode add(AstNode node){
    this.nodes.add(node);
    return this;
  }

  @Override
  public void visit(AstNodeVisitor visitor) {
    visitor.visitSequenceNode(this);
  }

  @Override
  public void visitChildren(AstNodeVisitor visitor) {
    for(AstNode node : this){
      node.visit(visitor);
    }
  }

  @Override
  public Iterator<AstNode> iterator() {
    return this.nodes.iterator();
  }
}