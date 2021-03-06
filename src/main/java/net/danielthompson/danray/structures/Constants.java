package net.danielthompson.danray.structures;

/**
 * DanRay
 * User: dthompson
 * Date: 7/9/13
 * Time: 5:09 PM
 */
public class Constants {

   public static final float MachineEpsilon = Math.ulp(1.0f) * 0.5f;

   public static final float Epsilon = .00001f;
   public static final float HalfEpsilon = Epsilon * .5f;
   public static final float DoubleEpsilon = Epsilon * 2f;
   public static final float UnitTestDelta = .001f;
   //public static final float Epsilon = .000000001265;
   //public static final float Epsilon = .0000000001006;

   public static final float NOHIT = -1.0f;

   public static final float PI = (float) Math.PI;
   public static final float OneOverPi = (float) (1.0 / Math.PI);
   public static final float OneOver2Pi = (float) (1.0 / (2 * Math.PI));
   public static final float OneOver4Pi = (float) (1.0 / (4 * Math.PI));

   public static final float PITimes2 = (float)(Math.PI * 2.0);

   public static final float PIOver2 = (float) (Math.PI / 2.0);
   public static final float PIOver3 = (float) (Math.PI / 3.0);
   public static final float PIOver4 = (float) (Math.PI / 4.0);
   public static final float PIOver6 = (float) (Math.PI / 6.0);
   public static final float PIOver12 = (float) (Math.PI / 12.0);

   public static final float IncidenceFactor = 100.0f / PIOver2;

   public static final float OneOver255 = 1.0f / 255;
   public static final float OneOver255f = 1.0f / 255.0f;

   public static final float Root2 = (float) Math.sqrt(2);
   public static final float Root3 = (float) Math.sqrt(3);

   public final static Vector NegativeX = new Vector(-1, 0, 0);
   public final static Vector NegativeY = new Vector(0, -1, 0);
   public final static Vector NegativeZ = new Vector(0, 0, -1);
   public final static Vector PositiveX = new Vector(1, 0, 0);
   public final static Vector PositiveY = new Vector(0, 1, 0);
   public final static Vector PositiveZ = new Vector(0, 0, 1);

   public static boolean WithinEpsilon(float number, float target) {
      return (number > target) ? (DoubleEpsilon + target >= number) : (DoubleEpsilon + number >= target);
   }

   public static boolean WithinEpsilon(float number, float target, float epsilon) {
      return (number > target) ? (2 * epsilon + target >= number) : (2 * epsilon + number >= target);
   }

   public static boolean WithinEpsilon(Point p1, Point p2) {
      return (WithinEpsilon(p1.X, p2.X, Epsilon)
             && WithinEpsilon(p1.Y, p2.Y, Epsilon)
             && WithinEpsilon(p1.Z, p2.Z, Epsilon));
   }


}
