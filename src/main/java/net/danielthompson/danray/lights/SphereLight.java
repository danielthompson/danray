package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.utility.GeometryCalculations;

import java.util.List;

/**
 * Created by daniel on 3/8/14.
 */
public class SphereLight extends AbstractLight {

   public Sphere Sphere;

   public SphereLight(Sphere sphere, SpectralPowerDistribution spd) {
      super(spd);
      Sphere = sphere;
      WorldBoundingBox = sphere.WorldBoundingBox;
      ObjectToWorld = sphere.ObjectToWorld;
      WorldToObject = sphere.WorldToObject;
   }

   @Override
   public void RecalculateWorldBoundingBox() {
      Sphere.RecalculateWorldBoundingBox();
      WorldBoundingBox = Sphere.WorldBoundingBox;

   }

   @Override
   public boolean Hits(Ray ray) {
      return Sphere.Hits(ray);
   }

   @Override
   public Intersection GetHitInfo(Ray ray) {
      Intersection state = Sphere.GetHitInfo(ray);
      if (state != null && state.Shape == Sphere)
         state.Shape = this;
      return state;
   }

   @Override
   public List<Intersection> GetAllHitPoints(Ray ray) {
      return null;
   }


   @Override
   public Point getRandomPointOnSurface() {

      float[] xyz = GeometryCalculations.randomPointOnSphere();

      Point point = new Point(xyz);

      point.Scale(Sphere.Radius);
      point.Plus(Sphere.Origin);

      if (Sphere.ObjectToWorld != null) {
         point = Sphere.ObjectToWorld.Apply(point);
      }

      return point;
   }

   @Override
   public Point getRandomPointOnSideOf(Vector side) {

      // this is all in world space
      Point surfacePoint = getRandomPointOnSurface();
      Vector directionFromSurfacePointToOrigin = Vector.Minus(surfacePoint, Sphere.Origin);
      if (directionFromSurfacePointToOrigin.Dot(side) < 0) {
         surfacePoint = new Point(-surfacePoint.X, -surfacePoint.Y, -surfacePoint.Z);
      }

      return surfacePoint;
   }

   @Override
   public Point getRandomPointOnSideOf(Point point) {
      if (Sphere.WorldToObject != null)
         point = Sphere.WorldToObject.Apply(point);

      Vector directionToPoint = Vector.Minus(point, Sphere.Origin);

      if (Sphere.ObjectToWorld != null)
         directionToPoint = Sphere.ObjectToWorld.Apply(directionToPoint);

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

      if (Sphere.ObjectToWorld != null)
         Sphere.ObjectToWorld.Apply(ray);
      return ray;
   }


   @Override
   public float getPDF(Point point, Vector directionFromLightToPoint) {

      Point origin = new Point(Sphere.Origin);

      if (Sphere.ObjectToWorld != null)
         origin = Sphere.ObjectToWorld.Apply(origin);

      float sqrDist = (float) point.SquaredDistanceBetween(origin);

      float sinThetaMax2 = (float) (Sphere.Radius * Sphere.Radius) / sqrDist;
      //float sinTheta = Math.sqrt(sinThetaMax2);
      float cosThetaMax = (float) Math.sqrt(Math.max(0, 1 - sinThetaMax2));

      float pdf = (float) (4.0f * Constants.PI / GeometryCalculations.UniformConePDF(cosThetaMax));

      return pdf; //TODO
   }



}
