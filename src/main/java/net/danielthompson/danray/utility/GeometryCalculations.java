package net.danielthompson.danray.utility;

import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import org.apache.commons.math3.util.FastMath;

import java.security.SecureRandom;
import java.util.Random;
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

   public static final Point3 randomPointOnSphere()
   {
      float x, y, z, d2;
      Random random = secureRandom.get();

      do {
         x = (float) random.nextGaussian();
         y = (float) random.nextGaussian();
         z = (float) random.nextGaussian();
         d2 = x*x + y*y + z*z;
      } while (d2 <= Float.MIN_NORMAL);
      float s = (float) Math.sqrt(1.0 / d2);
      return new Point3(x*s, y*s, z*s);
   }

   public static final Vector3 randomVectorOnSphere()
   {
      float x, y, z, d2;
      Random random = secureRandom.get();

      do {
         x = (float) random.nextGaussian();
         y = (float) random.nextGaussian();
         z = (float) random.nextGaussian();
         d2 = x*x + y*y + z*z;
      } while (d2 <= Float.MIN_NORMAL);
      float s = (float) Math.sqrt(1.0 / d2);
      return new Vector3(x*s, y*s, z*s);
   }

   private static float angleOfIncidencePercentageFactor = 10.f / 9.f;

   public static float GetAngleOfIncidence(Ray incomingRay, Intersection state) {
      Normal normal = state.normal;
      Vector3 incomingDirection = incomingRay.Direction;

      float angleRadians = (float) FastMath.acos(normal.dot(incomingDirection));

      float angleDegress = (float) FastMath.toDegrees(angleRadians);

      return 180 - angleDegress;
   }

   public static float radiansBetween(Vector3 v, Normal n) {
      Vector3 nv = Vector3.normalize(v);
      Normal nn = Normal.normalize(n);

      float angleRadians = (float) Math.acos(nv.dot(nn));

      return Math.abs(angleRadians);
   }

   public static float GetCosineWeightedIncidencePercentage(Vector3 incomingDirection, Normal normal) {
      float dot = normal.dot(incomingDirection);
      dot = (dot < 0) ? -dot : 0;
      return dot;
   }

   public static float GetAngleOfIncidencePercentage(Ray incomingRay, Intersection state) {
      float AoI = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);

      float AoIp = FastMath.abs(100 - (AoI * angleOfIncidencePercentageFactor));

      return AoIp;
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
