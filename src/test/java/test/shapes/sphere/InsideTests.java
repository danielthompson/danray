package test.shapes.sphere;

import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.shapes.Sphere;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 5:13 PM
 */
public class InsideTests {
   @Test
   public void testInside1() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      for (float i = 0; i < sphere.Radius / 2; i += .03) {
         for (float j = 0; j < sphere.Radius / 2; j += .03) {
            for (float k = 0; k < sphere.Radius / 2; k += .03) {
               Point point = new Point(i, j, k);
               Assert.assertTrue(sphere.Inside(point), "Point " + point + " is inside sphere.");
            }
         }
      }
   }

   @Test
   public void testInside2() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point point = new Point(2, 0, 0);

      Assert.assertFalse(sphere.Inside(point), "Point is outside sphere.");
   }
}
