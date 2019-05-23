package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector3;

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
   public Point3 getRandomPointOnSurface() {
      return null;
   }

   @Override
   public Point3 getRandomPointOnSideOf(Vector3 side) {
      return null;
   }

   @Override
   public Point3 getRandomPointOnSideOf(Point3 point) {
      return null;
   }

   @Override
   public Ray getRandomRayInPDF() {
      return null;
   }

   @Override
   public float getPDF(Point3 point, Vector3 directionFromLightToPoint) {
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
