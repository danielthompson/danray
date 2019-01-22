package net.danielthompson.danray.utility;

import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Vector;

import java.security.SecureRandom;

/**
 * Created by daniel on 8/29/15.
 */
public class MonteCarloCalculations {

   /**
    * Generates a location on the unit disc.
    * @return An array {x, y} such that (x, y) is inside the unit disc.
    */
   public static float[] RejectionSampleUnitDisc() {


      float x;
      float y;

      do {
         x = (float) (1 - 2 * GeometryCalculations.splitRandom.get().nextDouble());
         y = (float) (1 - 2 * GeometryCalculations.splitRandom.get().nextDouble());
      } while (x * x + y * y > 1);

      return new float[] {x, y};
   }

   public static Vector CosineSampleHemisphere() {

      float[] xz = RejectionSampleUnitDisc();
      float x = xz[0];
      float z = xz[1];

      float value = 1 - x * x - z * z;

      float y = (float) Math.sqrt(value);
      // max not needed due to RejectionSampleUnitDisc() implementation
//      float y = (float) Math.sqrt(Math.max(0, value));


      Vector ret = new Vector(x, y, z);

      return ret;
   }

   public static float CosineHemispherePDF(float cosTheta, float phi) {
      return cosTheta * Constants.OneOverPi;
   }
}
