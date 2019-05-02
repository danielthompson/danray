package net.danielthompson.danray.utility;

import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Vector;

import java.security.SecureRandom;
import java.util.SplittableRandom;

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

      SplittableRandom random = GeometryCalculations.splitRandom.get();

      do {
         x = (float) (1 - 2 * random.nextDouble());
         y = (float) (1 - 2 * random.nextDouble());
      } while (x * x + y * y > 1);

      return new float[] {x, y};
   }

   public static Vector CosineSampleHemisphere() {
      float x;
      float y;
      float z;

      SplittableRandom random = GeometryCalculations.splitRandom.get();

      do {
         x = (float) (1 - 2 * random.nextDouble());
         z = (float) (1 - 2 * random.nextDouble());
      } while (x * x + z * z > 1);

      float value = 1 - x * x - z * z;
      // due to rounding error, value can very occasionally be < 0, fix here
      value = value < 0 ? 0 : value;

      y = (float) Math.sqrt(value);

      assert !Float.isNaN(x);
      assert !Float.isNaN(y);
      assert !Float.isNaN(z);

      return new Vector(x, y, z);
   }

   public static float CosineHemispherePDF(float cosTheta, float phi) {
      return cosTheta * Constants.OneOverPi;
   }
}
