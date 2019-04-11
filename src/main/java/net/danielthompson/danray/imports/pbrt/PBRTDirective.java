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

   protected String getType(List<String> s) {
      if (s.get(1) != "\"") {
         Logger.Log(Logger.Level.Error, "Type string missing leading quote");
         throw new RuntimeException();
      }
      if (s.get(3) != "\"") {
         Logger.Log(Logger.Level.Error, "Type string missing trailing quote");
         throw new RuntimeException();
      }
      return s.get(2);
   }

   protected PBRTArgument<Float> getFloatArgument(List<String> s, int startIndex) {
      if (!s.get(startIndex).equals("\"")) {
         Logger.Log(Logger.Level.Error, "Type/name string missing leading quote");
         throw new RuntimeException();
      }
      if (!s.get(startIndex + 1).equals("float")) {
         Logger.Log(Logger.Level.Error, "Argument's expected type (float) doesn't match actual type (" + s.get(startIndex + 1) + ")" );
         throw new RuntimeException();
      }
      PBRTArgument<Float> argument = new PBRTArgument<>();

      argument.Name = s.get(startIndex + 2);

      if (!s.get(startIndex + 3).equals("\"")) {
         Logger.Log(Logger.Level.Error, "Type/name string missing ending quote");
         throw new RuntimeException();
      }

      float value;
      if (s.get(startIndex + 4).equals("[")) {
         value = parseFloat(s.get(startIndex + 5));
      }
      else {
         value = parseFloat(s.get(startIndex + 4));
      }

      argument.Values.add(value);

      return argument;
   }

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

   protected int parseInt(String s) {
      try {
         int value = Integer.parseInt(s);
         return value;
      }
      catch (NumberFormatException e) {
         Logger.Log(Logger.Level.Error, "Couldn't parse an int from [" + s + "]");
         throw e;
      }
   }
}

