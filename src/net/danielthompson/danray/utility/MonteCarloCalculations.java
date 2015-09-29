package net.danielthompson.danray.utility;

import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Vector;

import java.util.Random;

/**
 * Created by daniel on 8/29/15.
 */
public class MonteCarloCalculations {

   /**
    * Generates a location on the unit disc.
    * @return An array {x, y} such that (x, y) is inside the unit disc.
    */
   public static double[] RejectionSampleUnitDisc() {

      double x;
      double y;

      do {
         x = 1 - 2 * GeometryCalculations.random.nextDouble();
         y = 1 - 2 * GeometryCalculations.random.nextDouble();
      } while (x * x + y * y > 1);

      return new double[] {x, y};
   }

   public static Vector CosineSampleHemisphere() {

      double[] xy = RejectionSampleUnitDisc();
      double x = xy[0];
      double y = xy[1];
      double z = Math.sqrt(Math.max(0, 1 - x * x - y * y));

      Vector ret = new Vector(x, y, z);
      return ret;
   }

   public static double CosineHemispherePDF(double cosTheta, double phi) {
      return cosTheta * Constants.OneOverPi;
   }
}
