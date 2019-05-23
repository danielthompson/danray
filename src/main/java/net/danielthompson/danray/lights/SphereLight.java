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
   public Point3 getRandomPointOnSurface() {

      float[] xyz = GeometryCalculations.randomPointOnSphere();

      Point3 point = new Point3(xyz);

      point.scale(Sphere.Radius);
      point.plus(Sphere.Origin);

      if (Sphere.ObjectToWorld != null) {
         point = Sphere.ObjectToWorld.Apply(point);
      }

      return point;
   }

   @Override
   public Point3 getRandomPointOnSideOf(Vector side) {

      // this is all in world space
      Point3 surfacePoint = getRandomPointOnSurface();
      Vector directionFromSurfacePointToOrigin = Vector.Minus(surfacePoint, Sphere.Origin);
      if (directionFromSurfacePointToOrigin.Dot(side) < 0) {
         surfacePoint = new Point3(-surfacePoint.x, -surfacePoint.y, -surfacePoint.z);
      }

      return surfacePoint;
   }

   @Override
   public Point3 getRandomPointOnSideOf(Point3 point) {
      if (Sphere.WorldToObject != null)
         point = Sphere.WorldToObject.Apply(point);

      Vector directionToPoint = Vector.Minus(point, Sphere.Origin);

      if (Sphere.ObjectToWorld != null)
         directionToPoint = Sphere.ObjectToWorld.Apply(directionToPoint);

      Point3 result = getRandomPointOnSideOf(directionToPoint);
      return result;
   }


   @Override
   public Ray getRandomRayInPDF() {
      Point3 point = getRandomPointOnSurface();

      //point = new Point(1, 0, 0);

      Vector v = new Vector(point.x, point.y, point.z);

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
   public float getPDF(Point3 point, Vector directionFromLightToPoint) {

      Point3 origin = new Point3(Sphere.Origin);

      if (Sphere.ObjectToWorld != null)
         origin = Sphere.ObjectToWorld.Apply(origin);

      float sqrDist = (float) point.squaredDistanceBetween(origin);

      float sinThetaMax2 = (float) (Sphere.Radius * Sphere.Radius) / sqrDist;
      //float sinTheta = Math.sqrt(sinThetaMax2);
      float cosThetaMax = (float) Math.sqrt(Math.max(0, 1 - sinThetaMax2));

      float pdf = (float) (4.0f * Constants.PI / GeometryCalculations.UniformConePDF(cosThetaMax));

      return pdf; //TODO
   }



}
