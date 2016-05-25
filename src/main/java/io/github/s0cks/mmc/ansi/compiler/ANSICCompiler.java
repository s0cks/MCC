package io.github.s0cks.mmc.ansi.compiler;

import io.github.s0cks.mmc.Register;
import io.github.s0cks.mmc.ansi.ast.AstNodeVisitor;
import io.github.s0cks.mmc.ansi.ast.BinaryOpNode;
import io.github.s0cks.mmc.ansi.ast.LiteralNode;
import io.github.s0cks.mmc.ansi.ast.ReturnNode;
import io.github.s0cks.mmc.ansi.ast.SequenceNode;
import io.github.s0cks.mmc.ansi.type.CFunction;
import io.github.s0cks.mmc.ansi.type.CInteger;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public final class ANSICCompiler
implements AstNodeVisitor,
           Closeable{
  private final CFunction function;
  private final OutputStream os;
  private int indent = 0;

  public ANSICCompiler(CFunction function, OutputStream os){
    this.function = function;
    this.os = os;
    this.writeln("_" + function.name + ":");
  }

  private void indent(){
    try{
      for(int i = 0; i < this.indent; i++){
        this.os.write("  ".getBytes(StandardCharsets.UTF_8));
      }
    } catch(Exception e){
      throw new RuntimeException(e);
    }
  }

  private void write(String str){
    this.indent();
    try{
      this.os.write(str.getBytes(StandardCharsets.UTF_8));
    } catch(Exception e){
      throw new RuntimeException(e);
    }
  }

  private void writeln(String str){
    this.write(str + "\n");
  }

  private void writef(String format, Object... args){
    this.write(String.format(format, args));
  }

  @Override
  public void visitLiteralNode(LiteralNode node) {
    try{
      switch(node.literal.type){
        case INTEGER:{
          this.os.write((Integer.toString(((CInteger) node.literal).value)).getBytes(StandardCharsets.UTF_8));
          break;
        }
      }
    } catch(Exception e){
      throw new RuntimeException(e);
    }
  }

  @Override
  public void visitReturnNode(ReturnNode node) {
    node.visitChildren(this);
  }

  @Override
  public void visitSequenceNode(SequenceNode node) {
    this.indent++;
    this.write("push RBP\n");
    this.write("mov RBP, RSP\n");
    this.write("sub RBP, 0\n");
    this.write("\n");
    node.visitChildren(this);
    this.write("\n");
    this.write("mov RSP, RBP\n");
    this.write("pop RBP\n");
    this.write("ret");
    this.indent--;
  }

  @Override
  public void visitBinaryOpNode(BinaryOpNode node) {
    this.writef("mov %s, ", Register.RAX);
    node.left.visit(this);
    this.write("\n");

    this.writef("mov %s, ", Register.RBX);
    node.right.visit(this);
    this.write("\n");

    switch(node.kind){
      case ADD: this.writef("add %s, %s", Register.RAX, Register.RBX); break;
      case SUB: this.writef("sub %s, %s", Register.RAX, Register.RBX); break;
      case DIV: this.writef("div %s, %s", Register.RAX, Register.RBX); break;
      case MUL: this.writef("mul %s, %s", Register.RAX, Register.RBP); break;
      default: break;
    }
    this.write("\n");
  }

  @Override
  public void close()
  throws IOException {
    this.os.close();
  }
}