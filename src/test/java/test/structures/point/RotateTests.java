package test.structures.point;

import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * DanRay
 * User: dthompson
 * Date: 7/16/13
 * Time: 10:20 AM
 */
public class RotateTests {

   public double delta = .0000000000001;

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testRotate1() throws Exception {
      Point point = new Point(2, 0, 0);
      Ray axis = new Ray(new Point(0, 0, -2), new Vector(0, 0, 1));
      double theta = 45;

      Point actualRotated = Point.Rotate(point, axis, theta);

      Point expectedRotated = new Point(Math.sqrt(2), Math.sqrt(2), 0);

      Assert.assertEquals(actualRotated.X, expectedRotated.X, delta);
      Assert.assertEquals(actualRotated.Y, expectedRotated.Y, delta);
      Assert.assertEquals(actualRotated.Z, expectedRotated.Z, delta);
   }

   @Test
   public void testRotate2() throws Exception {
      Point point = new Point(2, 0, 0);
      Ray axis = new Ray(new Point(0, 0, -2), new Vector(0, 0, 1));
      double theta = 45;

      Point actualRotated = Point.Rotate(point, axis, theta);

      Point expectedRotated = new Point(Math.sqrt(2), Math.sqrt(2), 0);

      Assert.assertEquals(actualRotated.X, expectedRotated.X, delta);
      Assert.assertEquals(actualRotated.Y, expectedRotated.Y, delta);
      Assert.assertEquals(actualRotated.Z, expectedRotated.Z, delta);
   }
}
