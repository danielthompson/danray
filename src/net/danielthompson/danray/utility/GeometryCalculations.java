package net.danielthompson.danray.utility;

import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;
import org.apache.commons.math3.util.FastMath;

import java.util.Random;

/**
 * Created by daniel on 3/17/15.
 */
public class GeometryCalculations {

   public static Random random = new Random();

   public static double[][] RandomSpherePoints = getRandomSpherePoints();

   private static final int maxRandomSpherePoints = 1000000;

   private static volatile int randomSpherePointer = 0;

   private static double[][] getRandomSpherePoints() {
      double[][] n = new double[maxRandomSpherePoints][3];
            for (int i = 0; i < n.length; i++) {
         double[] p = randomPointOnSphere();
         Vector v = new Vector(p[0], p[1], p[2]);
         v.Normalize();
         n[i] = new double[] {v.X, v.Y, v.Z};
      }

      return n;
   }

   public static double[] randomPointOnPregeneratedSphere() {


      if (randomSpherePointer > maxRandomSpherePoints - 500)
         randomSpherePointer = 0;
      return RandomSpherePoints[randomSpherePointer++];
   }

   public static double[] randomPointOnSphere()
   {
      double x, y, z, d2;
      do {
         x = random.nextGaussian();
         y = random.nextGaussian();
         z = random.nextGaussian();
         d2 = x*x + y*y + z*z;
      } while (d2 <= Double.MIN_NORMAL);
      double s = Math.sqrt(1.0 / d2);
      return new double[] {x*s, y*s, z*s};
   }

   private static double angleOfIncidencePercentageFactor = 10. / 9.;

   public static double GetAngleOfIncidence(Ray incomingRay, IntersectionState state) {
      Normal normal = state.Normal;
      Vector incomingDirection = incomingRay.Direction;

      double angleRadians = FastMath.acos(normal.Dot(incomingDirection));

      double angleDegress = FastMath.toDegrees(angleRadians);

      return 180 - angleDegress;
   }

   public static double angleBetween(Vector v1, Vector v2) {

      Vector nv1 = Vector.Normalize(v1);
      Vector nv2 = Vector.Normalize(v2);

      double angleRadians = Math.acos(nv1.Dot(nv2));
      double angleDegrees = Math.toDegrees(angleRadians);

      return Math.abs(angleDegrees);
   }

   public static double angleBetween(Vector v, Normal n) {
      Vector nv = Vector.Normalize(v);
      Normal nn = Normal.Normalize(n);

      double angleRadians = Math.acos(nv.Dot(nn));
      double angleDegrees = Math.toDegrees(angleRadians);

      return Math.abs(angleDegrees);
   }

   public static double angleBetween(Normal n1, Normal n2) {

      Normal nn1 = Normal.Normalize(n1);
      Normal nn2 = Normal.Normalize(n2);

      double angleRadians = Math.acos(n1.Dot(n2));
      double angleDegrees = Math.toDegrees(angleRadians);

      return Math.abs(angleDegrees);
   }

   public static double GetAngleOfIncidencePercentage(Ray incomingRay, IntersectionState state) {
      double AoI = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);

      double AoIp = FastMath.abs(100 - (AoI * angleOfIncidencePercentageFactor));

      return AoIp;
   }

   public static Ray GetRefractedRay(IntersectionState state, Normal normal, Ray incomingRay, double oldIndexOfRefraction) {

      normal.Normalize();

      double n1 = oldIndexOfRefraction;
      double n2 = state.Drawable.GetMaterial()._indexOfRefraction;
      double nRatio = n1 / n2;

      double cosTheta1 = -normal.Dot(incomingRay.Direction);
      double cosTheta2 = Math.sqrt(1 - nRatio * nRatio * (1 - (cosTheta1 * cosTheta1)));

      Vector refractedDirection;

      if (cosTheta1 > 0) {
         refractedDirection = Vector.Plus(incomingRay.Scale(nRatio), new Vector(Normal.Scale(normal, nRatio * cosTheta1 - cosTheta2)));
      }
      else {
         refractedDirection = Vector.Plus(incomingRay.Scale(nRatio), new Vector(Normal.Scale(normal, nRatio * cosTheta1 + cosTheta2)));
      }

      Point offsetIntersection = Point.Plus(state.IntersectionPoint, Vector.Scale(refractedDirection, .0000001));

      return new Ray(offsetIntersection, refractedDirection);
   }

   public static Ray GetReflectedRay(Point intersectionPoint, Normal normal, Ray incomingRay) {
      normal.Normalize();
      double factor = incomingRay.Direction.Dot(normal) * 2;
      Vector scaled = new Vector(Normal.Scale(normal, factor));
      Vector direction = Vector.Minus(new Vector(0, 0, 0), Vector.Minus(scaled, incomingRay.Direction));

      Point offsetIntersection = Point.Plus(intersectionPoint, Vector.Scale(direction, Constants.Epsilon * 1000));

      //Point direction = normal.ScaleFromOrigin(incomingRay.Direction.Dot(normal.Direction) * 2).Minus(incomingRay.Direction);
      Ray reflectedRay = new Ray(offsetIntersection, direction);
      return reflectedRay;
   }

   public static Ray GetReflectedRayPerturbed(Point intersectionPoint, Normal normal, Ray incomingRay, double perturbation) {
      double factor = incomingRay.Direction.Dot(normal) * 2;
      Vector scaled = new Vector(Normal.Scale(normal, factor));
      Vector direction = Vector.Minus(new Vector(0, 0, 0), Vector.Minus(scaled, incomingRay.Direction));

      Vector perturbationDirection = new Vector(Math.random()*perturbation, Math.random()*perturbation, Math.random()*perturbation);

      if (perturbationDirection.Dot(normal) < 0)
         perturbationDirection.Scale(-1);

      Point offsetIntersection = Point.Plus(intersectionPoint, Vector.Scale(direction, .0000001));

      direction.Plus(perturbationDirection);

      //Point direction = normal.ScaleFromOrigin(incomingRay.Direction.Dot(normal.Direction) * 2).Minus(incomingRay.Direction);
      Ray reflectedRay = new Ray(offsetIntersection, direction);
      return reflectedRay;
   }


   public static Ray GetRandomRayInNormalHemisphere(Point intersectionPoint, Normal normal) {

      double[] xyz;

      //synchronized (mutex) {
         if (randomSpherePointer >= maxRandomSpherePoints - 5000)
            randomSpherePointer = 0;
         xyz = RandomSpherePoints[randomSpherePointer++];
      //}

      Vector direction = new Vector(xyz[0], xyz[1], xyz[2]);

      if (direction.Dot(normal) < 0)
         direction.Scale(-1);

      Point offsetIntersection = Point.Plus(intersectionPoint, Vector.Scale(direction, .0000001));

      Ray randomRay = new Ray(offsetIntersection, direction);
      return randomRay;
   }

   public static Ray GetReflectedRayPerturbedFromNormal(Point intersectionPoint, Normal normal, Ray incomingRay, double perturbation) {
      double factor = incomingRay.Direction.Dot(normal) * 2;
      Vector scaled = new Vector(Normal.Scale(normal, factor));
      Vector direction = Vector.Minus(new Vector(0, 0, 0), Vector.Minus(scaled, incomingRay.Direction));

      double x = (Math.random() - .5) * 2 * perturbation + normal.X;
      double y = (Math.random() - .5) * 2 * perturbation + normal.Y;
      double z = (Math.random() - .5) * 2 * perturbation + normal.Z;

      Vector perturbationPointOffset = new Vector(x, y, z);

      Point offsetIntersection = Point.Plus(intersectionPoint, Vector.Scale(direction, .0000001));

      Point perturbedDirectionPoint = Point.Plus(offsetIntersection, perturbationPointOffset);

      Vector perturbationDirection = Point.Minus(perturbedDirectionPoint, offsetIntersection);


      direction.Plus(perturbationDirection);

      //Point direction = normal.ScaleFromOrigin(incomingRay.Direction.Dot(normal.Direction) * 2).Minus(incomingRay.Direction);
      Ray reflectedRay = new Ray(offsetIntersection, direction);
      return reflectedRay;
   }

   public static double UniformConePDF(double cosThetaMax) {
      return 1.0 / (2.0 * Math.PI * (1.0 - cosThetaMax));
   }
}
