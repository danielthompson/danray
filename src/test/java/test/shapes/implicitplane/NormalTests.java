package test.shapes.implicitplane;

import net.danielthompson.danray.shapes.ImplicitPlane;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Normal;
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
      Point planeOrigin = new Point(0, 0, 0);
      Normal normal = new Normal(0, 1, 0);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point vectorOrigin = new Point(2, 2, 2);
      Vector vectorDirection = new Vector(0, -1, 0);

      Ray ray = new Ray(vectorOrigin, vectorDirection);

      Intersection state = plane.GetHitInfo(ray);

      Point expectedHitPoint = new Point(2, 0, 2);

      Assert.assertTrue(state.Hits);
      Assert.assertEquals(state.Location, expectedHitPoint);
   }
}
