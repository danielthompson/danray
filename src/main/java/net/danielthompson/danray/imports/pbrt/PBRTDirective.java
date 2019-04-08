package net.danielthompson.danray.imports.pbrt;

import java.util.List;

public abstract class PBRTDirective {
   protected final List<String> Words;
   public String Identifier;
   public String Type;

   public PBRTDirective(List<String> words) {

      this.Words = words;
   }

   @Override
   public String toString() {
      return Identifier;
   }

   public List<PBRTArgument> Arguments;
}
