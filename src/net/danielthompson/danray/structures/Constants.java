package net.danielthompson.danray.structures;

/**
 * DanRay
 * User: dthompson
 * Date: 7/9/13
 * Time: 5:09 PM
 */
public class Constants {
   public static final double NumericalDelta = .00000000001;

   public static boolean WithinDelta(double number, double target) {
      return WithinDelta(number, target, NumericalDelta);
   }

   public static boolean WithinDelta(double number, double target, double delta) {

      //return number == target;

      if (number > target) {
         return (delta >= number - target);
      }
      else
         return (delta >= target - number);
   }

   public static boolean WithinDelta(Point p1, Point p2) {
      return (WithinDelta(p1.X, p2.X) && WithinDelta(p1.Y, p2.Y) && WithinDelta(p1.Z, p2.Z));
   }
}
