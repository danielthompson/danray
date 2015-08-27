package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;
import net.danielthompson.danray.tracers.GeometryCalculations;
import org.apache.commons.math3.util.FastMath;

/**
 * Created by daniel on 3/8/14.
 */
public class SphereLight extends Sphere implements Radiatable {

   protected double Power;

   public SphereLight(double power) {
      Power = power;
   }

   public SphereLight(double power, Material material) {
      super(material);
      Power = power;

   }

   private static double[] randoms;

   static {
      randoms = new double[65536];

      for (int i = 0; i < 65536; i++) {
         randoms[i] = FastMath.random() * 2.0 - 1.0;
      }
   }

   private static int randomPointer;

   private static Object mutex = new Object();

   private Point getRandomPoint() {
      double x;
      double y;
      double z;

      //synchronized (mutex) {
      x = randoms[randomPointer];
      randomPointer = (randomPointer + 1) & 65535;
      y = randoms[randomPointer];
      randomPointer = (randomPointer + 1) & 65535;
      z = randoms[randomPointer];
      randomPointer = (randomPointer + 1) & 65535;
      //}

      Point point = new Point(x, y, z);
      return point;
   }

   @Override
   public Point getRandomPointOnSurface() {

      double[] xyz = GeometryCalculations.randomPointOnSphere();

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
      //ray.OffsetOriginForward(.00001);

      return ray;
   }

   @Override
   public double getPower() {
      return Power;
   }

   @Override
   public double getPDF(Point point, Vector directionFromLightToPoint) {

      Point origin = new Point(Origin);

      if (ObjectToWorld != null)
         origin = ObjectToWorld.Apply(origin);

      double sqrDist = point.SquaredDistanceBetween(origin);

      double sinThetaMax2 = Radius * Radius / sqrDist;
      double sinTheta = Math.sqrt(sinThetaMax2);
      double cosThetaMax = Math.sqrt(Math.max(0, 1 - sinThetaMax2));

      double pdf = GeometryCalculations.UniformConePDF(cosThetaMax);

      return pdf; //TODO
   }



}
