package test.shapes.sphere;

import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector3;
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

      Point3 Origin = new Point3(-10, 0, 0);
      Vector3 Direction = new Vector3(1, 0, 0);
      Ray ray = new Ray(Origin, Direction);

      boolean hits = sphere.Hits(ray);

      Point3 actualIntersection = sphere.GetHitInfo(ray).Location;
      Point3 expectedIntersection = new Point3(-1, 0, 0);

      AssertHelper.assertEquals(actualIntersection, expectedIntersection);
   }

   @Test
   public void testIntersect2() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Radius = 10;

      Point3 Origin = new Point3(0, 0, 0);
      Vector3 Direction = new Vector3(1, 0, 0);
      Ray ray = new Ray(Origin, Direction);

      boolean hits = sphere.Hits(ray);

      Intersection actualIntersectionState = sphere.GetHitInfo(ray);

      Point3 actualIntersection = actualIntersectionState.Location;
      Point3 expectedIntersection = new Point3(10, 0, 0);

      Assert.assertTrue(actualIntersectionState.Hits, "ray from inside sphere should hit sphere");
      Assert.assertEquals(ray.MinT, 10.0f, 10.f * Constants.UnitTestDelta);
      AssertHelper.assertEquals(actualIntersection, expectedIntersection);

   }
}