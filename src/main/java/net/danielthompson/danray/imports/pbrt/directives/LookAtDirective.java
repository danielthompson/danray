package net.danielthompson.danray.imports.pbrt.directives;

import net.danielthompson.danray.imports.pbrt.Constants;
import net.danielthompson.danray.imports.pbrt.PBRTDirective;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;

import java.util.List;

public class LookAtDirective extends PBRTDirective {

   public LookAtDirective(List<String> tokens) {
      super(tokens);
      this.Identifier = Constants.LookAt;
   }

   public Transform Parse() {
      float ex = Float.parseFloat(Words.get(1));
      float ey = Float.parseFloat(Words.get(2));
      float ez = Float.parseFloat(Words.get(3));
      float lx = Float.parseFloat(Words.get(4));
      float ly = Float.parseFloat(Words.get(5));
      float lz = Float.parseFloat(Words.get(6));
      float ux = Float.parseFloat(Words.get(7));
      float uy = Float.parseFloat(Words.get(8));
      float uz = Float.parseFloat(Words.get(9));

      Point eye = new Point(ex, ey, ez);
      Point lookAt = new Point(lx, ly, lz);
      Vector up = new Vector(ux, uy, uz);

      return Transform.LookAt(eye, lookAt, up);
   }
}
