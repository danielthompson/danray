package net.danielthompson.danray.utility;

import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import org.apache.commons.math3.util.FastMath;

import java.security.SecureRandom;
import java.util.SplittableRandom;


/**
 * Created by daniel on 3/17/15.
 */
public class GeometryCalculations {

   public static ThreadLocal<SplittableRandom> splitRandom = new ThreadLocal<>() {
      @Override protected SplittableRandom initialValue() {
         return new SplittableRandom();
      }
   };

   public static ThreadLocal<SecureRandom> secureRandom = new ThreadLocal<>() {
      @Override protected SecureRandom initialValue() {
         return new SecureRandom();
      }
   };

   public static float[] randomPointOnSphere()
   {
      float x, y, z, d2;
      do {
         x = (float) secureRandom.get().nextGaussian();
         y = (float) secureRandom.get().nextGaussian();
         z = (float) secureRandom.get().nextGaussian();
         d2 = x*x + y*y + z*z;
      } while (d2 <= Float.MIN_NORMAL);
      float s = (float) Math.sqrt(1.0 / d2);
      return new float[] {x*s, y*s, z*s};
   }

   private static float angleOfIncidencePercentageFactor = 10.f / 9.f;

   public static float GetAngleOfIncidence(Ray incomingRay, Intersection state) {
      Normal normal = state.Normal;
      Vector3 incomingDirection = incomingRay.Direction;

      float angleRadians = (float) FastMath.acos(normal.Dot(incomingDirection));

      float angleDegress = (float) FastMath.toDegrees(angleRadians);

      return 180 - angleDegress;
   }

   public static float radiansBetween(Vector3 v, Normal n) {
      Vector3 nv = Vector3.normalize(v);
      Normal nn = Normal.Normalize(n);

      float angleRadians = (float) Math.acos(nv.dot(nn));

      return Math.abs(angleRadians);
   }

   public static float GetCosineWeightedIncidencePercentage(Vector3 incomingDirection, Normal normal) {
      float dot = normal.Dot(incomingDirection);
      dot = (dot < 0) ? -dot : 0;
      return dot;
   }

   public static float GetIncidencePercentage(Vector3 incomingDirection, Normal normal) {
      float angleRadians = (float) FastMath.acos(normal.Dot(incomingDirection));
      angleRadians -= Constants.PIOver2;

      angleRadians *= Constants.IncidenceFactor;

      return angleRadians;

   }

   public static float GetAngleOfIncidencePercentage(Ray incomingRay, Intersection state) {
      float AoI = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);

      float AoIp = FastMath.abs(100 - (AoI * angleOfIncidencePercentageFactor));

      return AoIp;
   }

   public static Ray GetRefractedRay(Intersection state, Normal normal, Ray incomingRay, float oldIndexOfRefraction) {

      normal.Normalize();

      float n1 = oldIndexOfRefraction;
      float n2 = state.Shape.Material.IndexOfRefraction;
      float nRatio = n1 / n2;

      float cosTheta1 = -normal.Dot(incomingRay.Direction);
      float cosTheta2 = (float) Math.sqrt(1 - nRatio * nRatio * (1 - (cosTheta1 * cosTheta1)));

      Vector3 refractedDirection;

      if (cosTheta1 > 0) {
         refractedDirection = Vector3.plus(incomingRay.Scale(nRatio), new Vector3(Normal.Scale(normal, nRatio * cosTheta1 - cosTheta2)));
      }
      else {
         refractedDirection = Vector3.plus(incomingRay.Scale(nRatio), new Vector3(Normal.Scale(normal, nRatio * cosTheta1 + cosTheta2)));
      }

      Point3 offsetIntersection = Point3.plus(state.Location, Vector3.scale(refractedDirection, .0000001f));

      return new Ray(offsetIntersection, refractedDirection);
   }

   public static Ray GetReflectedRay(Point3 intersectionPoint, Normal normal, Ray incomingRay) {
      normal.Normalize();
      float factor = incomingRay.Direction.dot(normal) * 2;
      Vector3 scaled = new Vector3(Normal.Scale(normal, factor));
      Vector3 direction = Vector3.minus(new Vector3(0, 0, 0), Vector3.minus(scaled, incomingRay.Direction));

      Point3 offsetIntersection = Point3.plus(intersectionPoint, Vector3.scale(direction, Constants.Epsilon * 1000));

      //Point direction = normal.ScaleFromOrigin(incomingRay.Direction.dot(normal.Direction) * 2).minus(incomingRay.Direction);
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
