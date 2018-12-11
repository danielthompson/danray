package test.shapes.implicitplane;

import net.danielthompson.danray.shapes.ImplicitPlane;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * DanRay
 * User: dthompson
 * Date: 7/10/13
 * Time: 5:50 PM
 */
public class GetHitInfoTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testHitInfo1() throws Exception {

      Point planeOrigin = new Point(0, 0, 0);
      Normal normal = new Normal(0, 1, 0);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point vectorOrigin = new Point(2, 2, 2);
      Vector vectorDirection = new Vector(0, -1, 0);

      Ray ray = new Ray(vectorOrigin, vectorDirection);

      Intersection state = plane.getHitInfo(ray);

      Point expectedHitPoint = new Point(2, 0, 2);

      Assert.assertTrue(state.Hits);
      Assert.assertEquals(state.Location, expectedHitPoint);
   }

   @Test
       public void testHitInfo2() throws Exception {

      Point planeOrigin = new Point(0, 0, 0);
      Normal normal = new Normal(0, 0, 1);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point vectorOrigin = new Point(500, 500, 500);
      Vector vectorDirection = new Vector(0, 0, -1);

      Ray ray = new Ray(vectorOrigin, vectorDirection);

      Intersection state = plane.getHitInfo(ray);

      Point expectedHitPoint = new Point(500, 500, 0);

      Assert.assertTrue(state.Hits);
      Assert.assertEquals(state.Location, expectedHitPoint);
   }

   @Test
   public void testHitInfo3() throws Exception {

      Point planeOrigin = new Point(1, 0, 0);
      Normal normal = new Normal(1, 0, 1);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point vectorOrigin = new Point(500, 500, 500);
      Vector vectorDirection = new Vector(0, 0, -1);

      Ray ray = new Ray(vectorOrigin, vectorDirection);

      Intersection state = plane.getHitInfo(ray);

      Point expectedHitPoint = new Point(500, 500, -499);

      Assert.assertTrue(state.Hits);
      Assert.assertEquals(state.Location, expectedHitPoint);
   }

   @Test
   public void testHitInfo4() throws Exception {

      Point planeOrigin = new Point(0, 750, 0);
      Normal normal = new Normal(0, -1, 0);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point vectorOrigin = new Point(700, 500, 1600);
      Vector vectorDirection = new Vector(0, 0, -1);

      Ray ray = new Ray(vectorOrigin, vectorDirection);

      Intersection state = plane.getHitInfo(ray);

      //Point expectedHitPoint = new Point(500, 500, -499);

      Assert.assertFalse(state.Hits);
   }

   @Test
   public void testHitInfo5() throws Exception {

      Point planeOrigin = new Point(0, 750, 0);
      Normal normal = new Normal(0, -1, 0);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point vectorOrigin = new Point(700, 500, 1600);
      Vector vectorDirection = new Vector(0, -1, -1);

      Ray ray = new Ray(vectorOrigin, vectorDirection);

      Intersection state = plane.getHitInfo(ray);

      //Point expectedHitPoint = new Point(500, 500, -499);

      Assert.assertFalse(state.Hits);
   }

   @Test
   public void testHitFromBehindCase2() throws Exception {
      Point planeOrigin = new Point(5, 0, 0);
      Normal normal = new Normal(-1, -1, 0);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point vectorOrigin = new Point(1, 10, 0);
      Vector vectorDirection = new Vector(0, -1, 0);

      Ray ray = new Ray(vectorOrigin, vectorDirection);

      Intersection state = plane.getHitInfo(ray);

      //Point expectedHitPoint = new Point(500, 500, -499);

      Assert.assertTrue(state.Hits);
   }
}
