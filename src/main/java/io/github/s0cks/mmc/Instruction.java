package io.github.s0cks.mmc;

public enum Instruction{
  // Basic Math Operations
  NOP, // e.x: nop
  ADD, // e.x: add rax, 10
  SUB, // e.x: sub rax, 10
  MUL, // e.x: mul rax, 10
  DIV, // e.x: div rax, 10
  MOV, // e.x: mov rax, 10
  CMP, // e.x: cmp rax, rbx
  JMP, // e.x: jmp <label>
  PUSH, // e.x: push <value>
  POP, // e.x: pop <value>
  CALL, // e.x: call <value>
  RET, // e.x: ret
  SYSCALL; // e.x: syscall <value>

  @Override
  public String toString() {
    return this.name().toLowerCase();
  }
}