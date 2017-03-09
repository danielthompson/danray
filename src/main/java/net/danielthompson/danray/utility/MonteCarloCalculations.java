package net.danielthompson.danray.utility;

import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Vector;

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
         x = (float) (1 - 2 * GeometryCalculations.random.nextDouble());
         y = (float) (1 - 2 * GeometryCalculations.random.nextDouble());
      } while (x * x + y * y > 1);

      return new float[] {x, y};
   }

   public static Vector CosineSampleHemisphere() {

      float[] xy = RejectionSampleUnitDisc();
      float x = xy[0];
      float y = xy[1];
      float z = (float) Math.sqrt(Math.max(0, 1 - x * x - y * y));

      Vector ret = new Vector(x, y, z);
      return ret;
   }

   public static float CosineHemispherePDF(float cosTheta, float phi) {
      return cosTheta * Constants.OneOverPi;
   }
}
