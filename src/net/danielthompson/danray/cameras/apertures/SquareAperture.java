package net.danielthompson.danray.cameras.apertures;

import net.danielthompson.danray.structures.Point;

/**
 * Created by daniel on 3/4/14.
 */
public class SquareAperture implements Aperture {

   private double _size;

   public SquareAperture(double size) {
      _size = size;
   }

   @Override
   public Point GetOriginPoint() {
      double x = (Math.random() - .5) * _size;
      double y = (Math.random() - .5) * _size;
      double z = 0.0;

      return new Point(x, y, z);
   }
}
