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

   public final static Vector3 NegativeX = new Vector3(-1, 0, 0);
   public final static Vector3 NegativeY = new Vector3(0, -1, 0);
   public final static Vector3 NegativeZ = new Vector3(0, 0, -1);
   public final static Vector3 PositiveX = new Vector3(1, 0, 0);
   public final static Vector3 PositiveY = new Vector3(0, 1, 0);
   public final static Vector3 PositiveZ = new Vector3(0, 0, 1);

   public static boolean WithinEpsilon(final float number, final float target) {
      return (number > target) ? (DoubleEpsilon + target >= number) : (DoubleEpsilon + number >= target);
   }

   public static boolean WithinEpsilon(final float number, final float target, final float epsilon) {
      return (number > target) ? (DoubleEpsilon + target >= number) : (DoubleEpsilon + number >= target);
   }

   public static boolean WithinEpsilon(final Point3 p0, final Point3 p1) {
      return (WithinEpsilon(p0.x, p1.x, Epsilon)
             && WithinEpsilon(p0.y, p1.y, Epsilon)
             && WithinEpsilon(p0.z, p1.z, Epsilon));
   }


}
