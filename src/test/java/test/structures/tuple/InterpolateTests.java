package test.structures.tuple;

import net.danielthompson.danray.structures.Point3;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by daniel on 1/14/15.
 */
public class InterpolateTests {

   @Test
   public void testInterpolate1() {
      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(10, 10, 10);

      float percentage = 0.5f;

      Point3 result = Point3.lerp(p1, p2, percentage);

      Point3 expected = new Point3(5, 5, 5);

      Assert.assertEquals(result, expected);
   }

   @Test
   public void testInterpolate2() {
      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(10, 10, 10);

      float percentage = 0.6f;

      Point3 result = Point3.lerp(p1, p2, percentage);

      Point3 expected = new Point3(6, 6, 6);

      Assert.assertEquals(result, expected);
   }

   @Test
   public void testInterpolate3() {
      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(10, 10, 10);

      float percentage = 0.0f;

      Point3 result = Point3.lerp(p1, p2, percentage);

      Point3 expected = new Point3(0, 0, 0);

      Assert.assertEquals(result, expected);
   }

   @Test
   public void testInterpolate4() {
      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(10, 10, 10);

      float percentage = 1.0f;

      Point3 result = Point3.lerp(p1, p2, percentage);

      Point3 expected = new Point3(10, 10, 10);

      Assert.assertEquals(result, expected);
   }
}
