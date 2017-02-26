package test.shapes.sphere;

import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * DanRay
 * User: dthompson
 * Date: 6/27/13
 * Time: 12:37 PM
 */
public class IntersectTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testIntersect1() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point Origin = new Point(-10, 0, 0);
      Vector Direction = new Vector(1, 0, 0);
      Ray ray = new Ray(Origin, Direction);

      Point actualIntersection = sphere.getHitInfo(ray).IntersectionPoint;
      Point expectedIntersection = new Point(-1, 0, 0);

      Assert.assertEquals(actualIntersection.X, expectedIntersection.X, "X failed");
      Assert.assertEquals(actualIntersection.Y, expectedIntersection.Y, "Y failed");
      Assert.assertEquals(actualIntersection.Z, expectedIntersection.Z, "Z failed");
   }

   @Test
   public void testIntersect2() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 10;

      Point Origin = new Point(0, 0, 0);
      Vector Direction = new Vector(1, 0, 0);
      Ray ray = new Ray(Origin, Direction);

      IntersectionState actualIntersectionState = sphere.getHitInfo(ray);

      Point actualIntersection = actualIntersectionState.IntersectionPoint;
      Point expectedIntersection = new Point(10, 0, 0);

      Assert.assertTrue(actualIntersectionState.Hits, "ray from inside sphere should hit sphere");
      Assert.assertEquals(actualIntersectionState.TMin, 10.0, "T should be 10");
      Assert.assertEquals(actualIntersection.X, expectedIntersection.X, "X failed");
      Assert.assertEquals(actualIntersection.Y, expectedIntersection.Y, "Y failed");
      Assert.assertEquals(actualIntersection.Z, expectedIntersection.Z, "Z failed");
   }
}