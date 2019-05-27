package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector3;

import java.util.List;

/**
 * DanRay
 * User: dthompson
 * Date: 6/30/13
 * Time: 9:30
 */
public class PointLight extends AbstractLight {

   public Point3 Location;

   public PointLight(SpectralPowerDistribution spd, Point3 location) {
      super(spd);
      Location = location;
      WorldBoundingBox = new BoundingBox(location, location);
   }

   @Override
   public Point3 getRandomPointOnSurface() {
      return Location;
   }

   @Override
   public Point3 getRandomPointOnSideOf(Vector3 side) {
      return Location;
   }

   @Override
   public Point3 getRandomPointOnSideOf(Point3 point) {
      return Location;
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
   public void recalculateWorldBoundingBox() {
      WorldBoundingBox = new BoundingBox(Location, Location);
   }

   @Override
   public List<Intersection> intersectAll(Ray ray) {
      return null;
   }
}