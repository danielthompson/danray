package net.danielthompson.danray.cameras.apertures;

import net.danielthompson.danray.structures.Point3;

/**
 * Created by daniel on 3/4/14.
 */
public class CircleAperture extends Aperture {

   public CircleAperture(final float size) {
      Size = size;
   }

   @Override
   public Point3 GetOriginPoint() {

      // rejection sampling

      float x = 1.0f;
      float y = 1.0f;

      while (x * x + y * y > 1.0) {

         x = (float) (Math.random() * 2.0 - 1.0);
         y = (float) (Math.random() * 2.0 - 1.0);
      }

      x *= Size * .5;
      y *= Size * .5;
      final float z = 0.0f;

      return new Point3(x, y, z);
   }
}
