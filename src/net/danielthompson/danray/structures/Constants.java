package net.danielthompson.danray.structures;

/**
 * DanRay
 * User: dthompson
 * Date: 7/9/13
 * Time: 5:09 PM
 */
public class Constants {
   public static final double Epsilon = .0000000001;
   public static final double OneOverPi = 1.0 / Math.PI;
   public static final double OneOver2Pi = 1.0 / (2 * Math.PI);
   public static final double OneOver4Pi = 1.0 / (4 * Math.PI);

   public static boolean WithinEpsilon(double number, double target) {
      return WithinEpsilon(number, target, Epsilon);
   }

   public static boolean WithinEpsilon(double number, double target, double epsilon) {

      if (number > target) {
         return (epsilon >= number - target);
      }
      else
         return (epsilon >= target - number);
   }

   public static boolean WithinEpsilon(Point p1, Point p2) {
      return (WithinEpsilon(p1.X, p2.X, Epsilon)
             && WithinEpsilon(p1.Y, p2.Y, Epsilon)
             && WithinEpsilon(p1.Z, p2.Z, Epsilon));
   }


}
