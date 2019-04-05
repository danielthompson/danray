package net.danielthompson.danray.imports.pbrt;

import java.util.List;

public class PBRTDirective {
   public String Name;
   public String Identifier;

   @Override
   public String toString() {
      return Name;
   }

   public List<PBRTArgument> Arguments;
}
