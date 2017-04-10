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

   public static float[][] RandomSpherePoints = getRandomSpherePoints();

   private static final int maxRandomSpherePoints = 1000000;

   private static volatile int randomSpherePointer = 0;

   private static float[][] getRandomSpherePoints() {
      float[][] n = new float[maxRandomSpherePoints][3];
            for (int i = 0; i < n.length; i++) {
         float[] p = randomPointOnSphere();
         Vector v = new Vector(p[0], p[1], p[2]);
         v.Normalize();
         n[i] = new float[] {v.X, v.Y, v.Z};
      }

      return n;
   }

   public static float[] randomPointOnPregeneratedSphere() {


      if (randomSpherePointer > maxRandomSpherePoints - 499)
         randomSpherePointer = 0;
      return RandomSpherePoints[randomSpherePointer++];
   }

   public static float[] randomPointOnSphere()
   {
      float x, y, z, d2;
      do {
         x = (float) random.nextGaussian();
         y = (float) random.nextGaussian();
         z = (float) random.nextGaussian();
         d2 = x*x + y*y + z*z;
      } while (d2 <= Double.MIN_NORMAL);
      float s = (float) Math.sqrt(1.0 / d2);
      return new float[] {x*s, y*s, z*s};
   }

   private static float angleOfIncidencePercentageFactor = 10.f / 9.f;

   public static float GetAngleOfIncidence(Ray incomingRay, IntersectionState state) {
      Normal normal = state.Normal;
      Vector incomingDirection = incomingRay.Direction;

      float angleRadians = (float) FastMath.acos(normal.Dot(incomingDirection));

      float angleDegress = (float) FastMath.toDegrees(angleRadians);

      return 180 - angleDegress;
   }

   public static float radiansBetween(Vector v, Normal n) {
      Vector nv = Vector.Normalize(v);
      Normal nn = Normal.Normalize(n);

      float angleRadians = (float) Math.acos(nv.Dot(nn));

      return Math.abs(angleRadians);
   }

   public static float GetCosineWeightedIncidencePercentage(Vector incomingDirection, Normal normal) {
      float dot = normal.Dot(incomingDirection);
      dot = (dot < 0) ? -dot : 0;
      return dot;
   }

   public static float GetIncidencePercentage(Vector incomingDirection, Normal normal) {
      float angleRadians = (float) FastMath.acos(normal.Dot(incomingDirection));
      angleRadians -= Constants.PIOver2;

      angleRadians *= Constants.IncidenceFactor;

      return angleRadians;

   }

   public static float GetAngleOfIncidencePercentage(Ray incomingRay, IntersectionState state) {
      float AoI = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);

      float AoIp = FastMath.abs(100 - (AoI * angleOfIncidencePercentageFactor));

      return AoIp;
   }

   public static Ray GetRefractedRay(IntersectionState state, Normal normal, Ray incomingRay, float oldIndexOfRefraction) {

      normal.Normalize();

      float n1 = oldIndexOfRefraction;
      float n2 = state.Shape.Material._indexOfRefraction;
      float nRatio = n1 / n2;

      float cosTheta1 = -normal.Dot(incomingRay.Direction);
      float cosTheta2 = (float) Math.sqrt(1 - nRatio * nRatio * (1 - (cosTheta1 * cosTheta1)));

      Vector refractedDirection;

      if (cosTheta1 > 0) {
         refractedDirection = Vector.Plus(incomingRay.Scale(nRatio), new Vector(Normal.Scale(normal, nRatio * cosTheta1 - cosTheta2)));
      }
      else {
         refractedDirection = Vector.Plus(incomingRay.Scale(nRatio), new Vector(Normal.Scale(normal, nRatio * cosTheta1 + cosTheta2)));
      }

      Point offsetIntersection = Point.Plus(state.IntersectionPoint, Vector.Scale(refractedDirection, .0000001f));

      return new Ray(offsetIntersection, refractedDirection);
   }

   public static Ray GetReflectedRay(Point intersectionPoint, Normal normal, Ray incomingRay) {
      normal.Normalize();
      float factor = incomingRay.Direction.Dot(normal) * 2;
      Vector scaled = new Vector(Normal.Scale(normal, factor));
      Vector direction = Vector.Minus(new Vector(0, 0, 0), Vector.Minus(scaled, incomingRay.Direction));

      Point offsetIntersection = Point.Plus(intersectionPoint, Vector.Scale(direction, Constants.Epsilon * 1000));

      //Point direction = normal.ScaleFromOrigin(incomingRay.Direction.Dot(normal.Direction) * 2).Minus(incomingRay.Direction);
      Ray reflectedRay = new Ray(offsetIntersection, direction);
      return reflectedRay;
   }

   public static Ray GetReflectedRayPerturbed(Point intersectionPoint, Normal normal, Ray incomingRay, float perturbation) {
      float factor = incomingRay.Direction.Dot(normal) * 2;
      Vector scaled = new Vector(Normal.Scale(normal, factor));
      Vector direction = Vector.Minus(new Vector(0, 0, 0), Vector.Minus(scaled, incomingRay.Direction));

      Vector perturbationDirection = new Vector((float)Math.random()*perturbation, (float)Math.random()*perturbation, (float)Math.random()*perturbation);

      if (perturbationDirection.Dot(normal) < 0)
         perturbationDirection.Scale(-1);

      Point offsetIntersection = Point.Plus(intersectionPoint, Vector.Scale(direction, .0000001f));

      direction.Plus(perturbationDirection);

      //Point direction = normal.ScaleFromOrigin(incomingRay.Direction.Dot(normal.Direction) * 2).Minus(incomingRay.Direction);
      Ray reflectedRay = new Ray(offsetIntersection, direction);
      return reflectedRay;
   }


   public static Ray GetRandomRayInNormalHemisphere(Point intersectionPoint, Normal normal) {

      float[] xyz;

      //synchronized (mutex) {
         if (randomSpherePointer >= maxRandomSpherePoints - 5000)
            randomSpherePointer = 0;
         xyz = RandomSpherePoints[randomSpherePointer++];
      //}

      Vector direction = new Vector(xyz[0], xyz[1], xyz[2]);

      if (direction.Dot(normal) < 0)
         direction.Scale(-1);

      Point offsetIntersection = Point.Plus(intersectionPoint, Vector.Scale(direction, .0000001f));

      Ray randomRay = new Ray(offsetIntersection, direction);
      return randomRay;
   }

   public static Ray GetReflectedRayPerturbedFromNormal(Point intersectionPoint, Normal normal, Ray incomingRay, float perturbation) {
      float factor = incomingRay.Direction.Dot(normal) * 2;
      Vector scaled = new Vector(Normal.Scale(normal, factor));
      Vector direction = Vector.Minus(new Vector(0, 0, 0), Vector.Minus(scaled, incomingRay.Direction));

      float x = ((float)Math.random() - .5f) * 2 * perturbation + normal.X;
      float y = ((float)Math.random() - .5f) * 2 * perturbation + normal.Y;
      float z = ((float)Math.random() - .5f) * 2 * perturbation + normal.Z;

      Vector perturbationPointOffset = new Vector(x, y, z);

      Point offsetIntersection = Point.Plus(intersectionPoint, Vector.Scale(direction, .0000001f));

      Point perturbedDirectionPoint = Point.Plus(offsetIntersection, perturbationPointOffset);

      Vector perturbationDirection = Point.Minus(perturbedDirectionPoint, offsetIntersection);


      direction.Plus(perturbationDirection);

      //Point direction = normal.ScaleFromOrigin(incomingRay.Direction.Dot(normal.Direction) * 2).Minus(incomingRay.Direction);
      Ray reflectedRay = new Ray(offsetIntersection, direction);
      return reflectedRay;
   }

   public static float clamp(float var0, float var1, float var2) {
      return var1 < var0 ? var0 : (var1 > var2 ? var2 : var1);
   }

   public static float UniformConePDF(float cosThetaMax) {
      return 1.0f / (2.0f * Constants.PI * (1.0f - cosThetaMax));
   }

   public static float Lerp(float v1, float w1, float v2, float w2) {
      return v1 * w1 + v2 * w2;
   }
}
