package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;

/**
 * Created by daniel on 6/25/16.
 */
public class ShapeLight extends ILight {

   public AbstractShape Shape;

   public ShapeLight(SpectralPowerDistribution spd, AbstractShape shape) {
      super(spd);
      Shape = shape;
   }

   @Override
   public Point getRandomPointOnSurface() {
      return null;
   }

   @Override
   public Point getRandomPointOnSideOf(Vector side) {
      return null;
   }

   @Override
   public Point getRandomPointOnSideOf(Point point) {
      return null;
   }

   @Override
   public Ray getRandomRayInPDF() {
      return null;
   }

   @Override
   public float getPDF(Point point, Vector directionFromLightToPoint) {
      return 0;
   }
}
