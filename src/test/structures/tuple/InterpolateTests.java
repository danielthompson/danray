package test.structures.tuple;

import net.danielthompson.danray.structures.Point;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by daniel on 1/14/15.
 */
public class InterpolateTests {

   @Test
   public void testInterpolate1() {
      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(10, 10, 10);

      double percentage = 0.5;

      Point result = Point.Interpolate(p1, p2, percentage);

      Point expected = new Point(5, 5, 5);

      Assert.assertEquals(result, expected);
   }

   @Test
   public void testInterpolate2() {
      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(10, 10, 10);

      double percentage = 0.6;

      Point result = Point.Interpolate(p1, p2, percentage);

      Point expected = new Point(6, 6, 6);

      Assert.assertEquals(result, expected);
   }

   @Test
   public void testInterpolate3() {
      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(10, 10, 10);

      double percentage = 0.0;

      Point result = Point.Interpolate(p1, p2, percentage);

      Point expected = new Point(0, 0, 0);

      Assert.assertEquals(result, expected);
   }

   @Test
   public void testInterpolate4() {
      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(10, 10, 10);

      double percentage = 1.0;

      Point result = Point.Interpolate(p1, p2, percentage);

      Point expected = new Point(10, 10, 10);

      Assert.assertEquals(result, expected);
   }
}
