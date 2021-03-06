package net.danielthompson.danray.cameras.apertures;

import net.danielthompson.danray.structures.Point;

/**
 * Created by daniel on 3/4/14.
 */
public class SquareAperture extends Aperture {

   public SquareAperture(float size) {
      Size = size;
   }

   @Override
   public Point GetOriginPoint() {
      float x = (float) ((Math.random() - .5) * Size);
      float y = (float) ((Math.random() - .5) * Size);
      float z = 0.0f;

      return new Point(x, y, z);
   }
}
