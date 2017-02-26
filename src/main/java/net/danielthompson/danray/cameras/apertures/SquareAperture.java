package net.danielthompson.danray.cameras.apertures;

import net.danielthompson.danray.structures.Point;

/**
 * Created by daniel on 3/4/14.
 */
public class SquareAperture extends Aperture {

   public SquareAperture(double size) {
      Size = size;
   }

   @Override
   public Point GetOriginPoint() {
      double x = (Math.random() - .5) * Size;
      double y = (Math.random() - .5) * Size;
      double z = 0.0;

      return new Point(x, y, z);
   }
}
