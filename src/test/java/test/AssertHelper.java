package test;

import net.danielthompson.danray.structures.*;
import org.testng.Assert;

/**
 * Created by daniel on 2/26/17.
 */
public class AssertHelper {
   public static void assertEquals(Ray expected, Ray actual) {
      assertEquals(expected.Origin, actual.Origin);
      assertEquals(expected.Direction, actual.Direction);
   }

   public static void assertEquals(BoundingBox expected, BoundingBox actual) {
      assertEquals(expected.point1, actual.point1);
      assertEquals(expected.point2, actual.point2);
   }

   public static void assertEquals(Point expected, Point actual) {
      Assert.assertEquals(expected.X, actual.X, Constants.Epsilon);
      Assert.assertEquals(expected.Y, actual.Y, Constants.Epsilon);
      Assert.assertEquals(expected.Z, actual.Z, Constants.Epsilon);
   }

   public static void assertEquals(Vector expected, Vector actual) {
      Assert.assertEquals(expected.X, actual.X, Constants.Epsilon);
      Assert.assertEquals(expected.Y, actual.Y, Constants.Epsilon);
      Assert.assertEquals(expected.Z, actual.Z, Constants.Epsilon);
   }

   public static void assertEquals(Normal expected, Normal actual) {
      Assert.assertEquals(expected.X, actual.X, Constants.Epsilon);
      Assert.assertEquals(expected.Y, actual.Y, Constants.Epsilon);
      Assert.assertEquals(expected.Z, actual.Z, Constants.Epsilon);
   }
}
