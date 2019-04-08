package net.danielthompson.danray.imports.pbrt;

import net.danielthompson.danray.Logger;

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

   protected float parseFloat(String s) {
      try {
         float value = Float.parseFloat(s);
         return value;
      }
      catch (NumberFormatException e) {
         Logger.Log(Logger.Level.Error, "Couldn't parse a float from [" + s + "]");
         throw e;
      }
   }
}
