package net.danielthompson.danray.cameras.apertures;

import net.danielthompson.danray.structures.Point;

/**
 * Created by daniel on 3/4/14.
 */
public class CircleAperture extends Aperture {

   public CircleAperture(double size) {
      Size = size;
   }

   /*
   @Override
   public Point GetOriginPoint() {
      double x = (Math.random() * 2.0 - 1.0);

      double y = (Math.random() * 2.0 - 1.0) * Math.sqrt(1.0 - x * x);

      x *= _size;
      y *= _size;

      double z = 0.0f;

      return new Point(x, y, z);
   }*/

   @Override
   public Point GetOriginPoint() {

      // rejection sampling

      double x = 1.0;
      double y = 1.0;

      while (x * x + y * y > 1.0) {

         x = (Math.random() * 2.0 - 1.0);
         y = (Math.random() * 2.0 - 1.0);
      }

      x *= Size * .5;
      y *= Size * .5;
      double z = 0.0f;

      return new Point(x, y, z);
   }
}
