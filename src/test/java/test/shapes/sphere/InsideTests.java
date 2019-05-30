package test.shapes.sphere;

import net.danielthompson.danray.structures.Point3;
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
      sphere.Origin = new Point3(0, 0, 0);
      sphere.Radius = 1;

      for (float i = 0; i < sphere.Radius / 2; i += .03) {
         for (float j = 0; j < sphere.Radius / 2; j += .03) {
            for (float k = 0; k < sphere.Radius / 2; k += .03) {
               Point3 point = new Point3(i, j, k);
               Assert.assertTrue(sphere.inside(point), "Point " + point + " is inside sphere.");
            }
         }
      }
   }

   @Test
   public void testInside2() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point3(0, 0, 0);
      sphere.Radius = 1;

      Point3 point = new Point3(2, 0, 0);

      Assert.assertFalse(sphere.inside(point), "Point is outside sphere.");
   }
}
