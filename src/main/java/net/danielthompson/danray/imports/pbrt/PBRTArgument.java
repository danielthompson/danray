package net.danielthompson.danray.imports.pbrt;

import java.util.ArrayList;
import java.util.List;

public class PBRTArgument<T> {
   public String Name;
   public List<T> Values = new ArrayList<>();

   @Override
   public String toString() {
      return Name;
   }
}
