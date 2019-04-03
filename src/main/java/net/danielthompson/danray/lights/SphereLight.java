package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.utility.GeometryCalculations;

/**
 * Created by daniel on 3/8/14.
 */
public class SphereLight extends Sphere implements ILight {

   public SphereLight(Transform[] transforms, SpectralPowerDistribution spd) {
      super(transforms, null);
      this.spd = spd;
   }

   @Override
   public Point getRandomPointOnSurface() {

      float[] xyz = GeometryCalculations.randomPointOnSphere();

      Point point = new Point(xyz);

      point.Scale(Radius);
      point.Plus(Origin);

      if (ObjectToWorld != null) {
         point = ObjectToWorld.Apply(point);
      }

      return point;
   }

   @Override
   public Point getRandomPointOnSideOf(Vector side) {

      // this is all in world space
      Point surfacePoint = getRandomPointOnSurface();
      Vector directionFromSurfacePointToOrigin = Vector.Minus(surfacePoint, Origin);
      if (directionFromSurfacePointToOrigin.Dot(side) < 0) {
         surfacePoint = new Point(-surfacePoint.X, -surfacePoint.Y, -surfacePoint.Z);
      }

      return surfacePoint;
   }

   @Override
   public Point getRandomPointOnSideOf(Point point) {
      if (WorldToObject != null)
         point = WorldToObject.Apply(point);

      Vector directionToPoint = Vector.Minus(point, Origin);

      if (ObjectToWorld != null)
         directionToPoint = ObjectToWorld.Apply(directionToPoint);

      Point result = getRandomPointOnSideOf(directionToPoint);
      return result;
   }

   @Override
   public Ray getRandomRayInPDF() {
      Point point = getRandomPointOnSurface();

      //point = new Point(1, 0, 0);

      Vector v = new Vector(point.X, point.Y, point.Z);

      Vector direction = new Vector(GeometryCalculations.randomPointOnSphere());

      if (v.Dot(direction) < 0)
         direction.Scale(-1);

      Ray ray = new Ray(point, direction);
      ray.OffsetOriginForward(.00001f);

      if (ObjectToWorld != null)
         ObjectToWorld.Apply(ray);
      return ray;
   }

   @Override
   public float getPDF(Point point, Vector directionFromLightToPoint) {

      Point origin = new Point(Origin);

      if (ObjectToWorld != null)
         origin = ObjectToWorld.Apply(origin);

      float sqrDist = (float) point.SquaredDistanceBetween(origin);

      float sinThetaMax2 = (float) (Radius * Radius) / sqrDist;
      //float sinTheta = Math.sqrt(sinThetaMax2);
      float cosThetaMax = (float) Math.sqrt(Math.max(0, 1 - sinThetaMax2));

      float pdf = (float) (4.0f * Constants.PI / GeometryCalculations.UniformConePDF(cosThetaMax));

      return pdf; //TODO
   }
}
