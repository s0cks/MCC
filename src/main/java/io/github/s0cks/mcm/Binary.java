package io.github.s0cks.mcm;

import java.util.Iterator;

public final class Binary
implements Iterable<Short>,
           Iterator<Short>{
  private final short[] code = new short[65535];
  private int pc = 0;
  private int size = 0;

  public Binary(){
    for(int i = 0; i < 65535; i++){
      this.code[i] = -1;
    }
  }

  public void append(short op){
    this.code[this.pc] = op;
    this.pc++;
  }

  public void set(short op){
    this.code[this.pc] = op;
  }

  public int size(){
    return this.size;
  }

  public void jmp(int amount){
    this.pc += amount;
  }

  public Binary reset(){
    this.size = this.pc;
    this.pc = 0;
    return this;
  }

  public short get(int index){
    return this.code[index];
  }

  public int counter(){
    return this.pc;
  }

  @Override
  public Iterator<Short> iterator() {
    this.pc = 0;
    return this;
  }

  @Override
  public boolean hasNext() {
    return this.pc <= this.code.length;
  }

  @Override
  public Short next() {
    short result = this.code[this.pc];
    this.pc++;
    return result;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("cannot remove from binary");
  }
}