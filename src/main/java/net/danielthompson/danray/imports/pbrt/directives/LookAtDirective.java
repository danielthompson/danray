package net.danielthompson.danray.imports.pbrt.directives;

import net.danielthompson.danray.imports.pbrt.Constants;
import net.danielthompson.danray.imports.pbrt.PBRTArgument;
import net.danielthompson.danray.imports.pbrt.PBRTDirective;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;

import java.util.List;

public class LookAtDirective extends PBRTDirective {

   PBRTArgument<Float> Argument;

   public LookAtDirective(List<String> tokens) {
      super(tokens);
      this.Identifier = Constants.LookAt;

      Argument = new PBRTArgument<>();

      if (tokens.size() != 10) {
         throw new RuntimeException("LookAt directive should have exactly 10 tokens");
      }

      for (int i = 1; i <= 9; i++) {
         float value = parseFloat(Words.get(i));
         Argument.Values.add(value);
      }
   }

   public Transform Parse() {
      Point eye =    new Point(Argument.Values.get(0), Argument.Values.get(1), Argument.Values.get(2));
      Point lookAt = new Point(Argument.Values.get(3), Argument.Values.get(4), Argument.Values.get(5));
      Vector up =    new Vector(Argument.Values.get(6), Argument.Values.get(7), Argument.Values.get(8));
      return Transform.LookAt(eye, lookAt, up);
   }
}
