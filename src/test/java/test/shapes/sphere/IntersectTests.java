package test.shapes.sphere;

import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.AssertHelper;

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

      Point Origin = new Point(-10, 0, 0);
      Vector Direction = new Vector(1, 0, 0);
      Ray ray = new Ray(Origin, Direction);

      boolean hits = sphere.hits(ray);

      Point actualIntersection = sphere.getHitInfo(ray).IntersectionPoint;
      Point expectedIntersection = new Point(-1, 0, 0);

      AssertHelper.assertEquals(actualIntersection, expectedIntersection);
   }

   @Test
   public void testIntersect2() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Radius = 10;

      Point Origin = new Point(0, 0, 0);
      Vector Direction = new Vector(1, 0, 0);
      Ray ray = new Ray(Origin, Direction);

      boolean hits = sphere.hits(ray);

      IntersectionState actualIntersectionState = sphere.getHitInfo(ray);

      Point actualIntersection = actualIntersectionState.IntersectionPoint;
      Point expectedIntersection = new Point(10, 0, 0);

      Assert.assertTrue(actualIntersectionState.Hits, "ray from inside sphere should hit sphere");
      Assert.assertEquals(ray.MinT, 10.0f, 10.f * Constants.UnitTestDelta);
      AssertHelper.assertEquals(actualIntersection, expectedIntersection);

   }
}