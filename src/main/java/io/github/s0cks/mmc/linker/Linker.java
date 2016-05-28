package io.github.s0cks.mmc.linker;

import io.github.s0cks.mmc.Binary;
import io.github.s0cks.mmc.assembler.Parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public final class Linker{
  private final Map<Integer, Integer> relocationTable = new HashMap<>();

  public Binary link(InputStream in, SimpleObjectFile... files)
  throws Exception {
    Binary bin = new Binary();
    Parser parser = new Parser(in);

    bin.append(((short) 0x0));
    bin.append(((short) 0x0));

    for(SimpleObjectFile sof : files){
      for(Map.Entry<String, Integer> symbol : sof.symbols.entrySet()){
        this.relocationTable.put(symbol.getValue() + 1, 0);
        parser.defineSymbol(symbol.getKey(), symbol.getValue());
      }
    }

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    parser.compile(bos);
    SimpleObjectFile sof = new SimpleObjectFile(new ByteArrayInputStream(bos.toByteArray()));

    for(SimpleObjectFile s : files){
      for(int i = 0; i < s.size; i++){
        if(this.relocationTable.containsKey(s.binary[i])){
          this.relocationTable.put(s.binary[i], bin.counter());
        }
        bin.append(((short) s.binary[i]));
      }
    }

    int newEntry = bin.counter();
    bin.setAt(0x0, ((short) (0x01C1 | (0x1F << 10))));
    bin.setAt(0x1, ((short) newEntry));

    for(int i = 0; i < sof.size; i++){
      if (this.relocationTable.containsKey(sof.binary[i] - 1)){
        bin.append(this.relocationTable.get(sof.binary[i] - 1).shortValue());
      } else{
        bin.append(((short) sof.binary[i]));
      }
    }

    return bin;
  }

  public static final class SimpleObjectFile{
    private final Map<String, Integer> symbols = new HashMap<>();
    private final int[] binary = new int[65535];
    private int size = 0;
    private int section = -1;

    public SimpleObjectFile(InputStream in)
    throws Exception{
      if(in == null) throw new NullPointerException("in == null");

      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line;
      while((line = reader.readLine()) != null){
        if(line.equals("symbols:")){
          this.section = 0x0;
          continue;
        } else if(line.equals("binary:")){
          this.section = 0x1;
          continue;
        } else if(line.equals("end")){
          break;
        }

        switch(this.section){
          case 0x0:{
            String[] parts = line.split(" " );
            this.symbols.put(parts[0], Integer.parseInt(parts[1].substring(2), 16) - 1);
            break;
          }
          case 0x1:{
            String[] parts = line.split(" ");
            for(String str : parts){
              if(str.isEmpty()) break;
              this.binary[this.size] = Integer.parseInt(str.substring(2), 16);
              this.size++;
            }
            break;
          }
        }
      }
    }
  }
}