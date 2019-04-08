package net.danielthompson.danray.imports.pbrt;

import java.util.ArrayList;
import java.util.List;

public class PBRTArgument {
   public String Name;
   public String Type;
   public List<String> Values = new ArrayList<>();

   @Override
   public String toString() {
      return Name;
   }
}
