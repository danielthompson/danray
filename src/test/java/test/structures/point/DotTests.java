package test.structures.point;

import net.danielthompson.danray.structures.Point3;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * DanRay
 * User: dthompson
 * Date: 6/27/13
 * Time: 12:46 PM
 */
public class DotTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testDot1() throws Exception {
      Point3 point1 = new Point3(1, 2, 3);
      Point3 point2 = new Point3(4, 5, 6);

      Assert.assertTrue(point1.dot(point2) == 32);
   }

   @Test
   public void testDot2() throws Exception {
      Point3 point1 = new Point3(-1, 2, 3);
      Point3 point2 = new Point3(4, 5, 6);

      Assert.assertTrue(point1.dot(point2) == 24);
   }

   @Test
   public void testDot3() throws Exception {
      Point3 point1 = new Point3(0, 0, 0);
      Point3 point2 = new Point3(0, 0, 0);

      Assert.assertTrue(point1.dot(point2) == 0);
   }
}
