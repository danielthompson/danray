package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;

/**
 * DanRay
 * User: dthompson
 * Date: 6/30/13
 * Time: 9:30
 */
public class PointLight extends AbstractLight {

   public Point Location;

   public PointLight(SpectralPowerDistribution spd, Point location) {
      super(spd);
      Location = location;
      WorldBoundingBox = new BoundingBox(location, location);
   }

   @Override
   public Point getRandomPointOnSurface() {
      return Location;
   }

   @Override
   public Point getRandomPointOnSideOf(Vector side) {
      return Location;
   }

   @Override
   public Point getRandomPointOnSideOf(Point point) {
      return Location;
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
      WorldBoundingBox = new BoundingBox(Location, Location);
   }
}