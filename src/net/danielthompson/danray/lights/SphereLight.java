package net.danielthompson.danray.lights;

import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;
import net.danielthompson.danray.utility.GeometryCalculations;
import org.apache.commons.math3.util.FastMath;

/**
 * Created by daniel on 3/8/14.
 */
public class SphereLight extends AbstractLight {

   public Sphere Sphere;

   public SphereLight(SpectralPowerDistribution spd, Sphere sphere) {
      super(spd);
      Sphere = sphere;
   }

   private static double[] randoms;

   static {
      randoms = new double[65536];

      for (int i = 0; i < 65536; i++) {
         randoms[i] = FastMath.random();
      }
   }

   private static int randomPointer;

   @Override
   public IntersectionState getHitInfo(Ray ray) {
      IntersectionState state = Sphere.getHitInfo(ray);
      if (state != null && state.Shape == Sphere)
         state.Shape = this;
      return state;
   }

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
   public BoundingBox GetWorldBoundingBox() {
      return Sphere.GetWorldBoundingBox();
   }

   @Override
   public Point getRandomPointOnSurface() {

      double[] xyz = GeometryCalculations.randomPointOnPregeneratedSphere();

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

      Vector direction = new Vector(GeometryCalculations.randomPointOnPregeneratedSphere());

      if (v.Dot(direction) < 0)
         direction.Scale(-1);

      Ray ray = new Ray(point, direction);
      ray.OffsetOriginForward(.00001);

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
      //double sinTheta = Math.sqrt(sinThetaMax2);
      float cosThetaMax = (float) Math.sqrt(Math.max(0, 1 - sinThetaMax2));

      float pdf = (float) (4.0f * Math.PI / GeometryCalculations.UniformConePDF(cosThetaMax));

      return pdf; //TODO
   }



}
