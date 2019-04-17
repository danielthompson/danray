package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;

import java.util.List;

/**
 * Created by daniel on 6/25/16.
 */
public class ShapeLight extends AbstractLight {

   public AbstractShape Shape;

   public ShapeLight(SpectralPowerDistribution spd, AbstractShape shape) {
      super(spd);
      Shape = shape;
      WorldBoundingBox = shape.WorldBoundingBox;
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

   @Override
   public void RecalculateWorldBoundingBox() {
      Shape.RecalculateWorldBoundingBox();
      WorldBoundingBox = Shape.WorldBoundingBox;
   }

   @Override
   public List<Intersection> GetAllHitPoints(Ray ray) {
      return null;
   }
}
