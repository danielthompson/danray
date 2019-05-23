package test;

import net.danielthompson.danray.structures.*;
import org.testng.Assert;

/**
 * Created by daniel on 2/26/17.
 */
public class AssertHelper {
   public static void assertEquals(Ray actual, Ray expected) {
      AssertHelper.assertEquals(actual.Origin, expected.Origin);
      AssertHelper.assertEquals(actual.Direction, expected.Direction);
   }

   public static void assertEquals(BoundingBox actual, BoundingBox expected) {
      AssertHelper.assertEquals(actual.point1, expected.point1);
      AssertHelper.assertEquals(actual.point2, expected.point2);
   }

   public static void assertEquals(Point3 actual, Point3 expected) {
      AssertHelper.assertEquals(actual.x, expected.x);
      AssertHelper.assertEquals(actual.y, expected.y);
      AssertHelper.assertEquals(actual.z, expected.z);
   }

   public static void assertEquals(Vector3 actual, Vector3 expected) {
      AssertHelper.assertEquals(actual.x, expected.x);
      AssertHelper.assertEquals(actual.y, expected.y);
      AssertHelper.assertEquals(actual.z, expected.z);
   }

   public static void assertEquals(Normal actual, Normal expected) {
      AssertHelper.assertEquals(actual.x, expected.x);
      AssertHelper.assertEquals(actual.y, expected.y);
      AssertHelper.assertEquals(actual.z, expected.z);
   }

   public static void assertEquals(float actual, float expected) {
      float delta = Constants.UnitTestDelta;

      if (Math.abs(expected) > 1.0f)
         delta = delta * expected;

      delta = Math.abs(delta);

      AssertHelper.assertEquals(actual, expected, delta);
   }

   private static void assertEquals(float actual, float expected, float delta) {

      Assert.assertEquals(actual, expected, delta);
   }
}
