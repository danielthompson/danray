package test.shapes.implicitplane;

import net.danielthompson.danray.shapes.ImplicitPlane;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector3;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * DanRay
 * User: dthompson
 * Date: 7/11/13
 * Time: 1:35 PM
 */
public class NormalTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }


   @Test
   public void testNormal1() throws Exception {
      Point3 planeOrigin = new Point3(0, 0, 0);
      Normal normal = new Normal(0, 1, 0);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point3 vectorOrigin = new Point3(2, 2, 2);
      Vector3 vectorDirection = new Vector3(0, -1, 0);

      Ray ray = new Ray(vectorOrigin, vectorDirection);

      Intersection state = plane.GetHitInfo(ray);

      Point3 expectedHitPoint = new Point3(2, 0, 2);

      Assert.assertTrue(state.Hits);
      Assert.assertEquals(state.Location, expectedHitPoint);
   }
}
