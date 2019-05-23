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

      Point3 planeOrigin = new Point3(0, 0, 0);
      Normal normal = new Normal(0, 1, 0);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point3 vectorOrigin = new Point3(2, 2, 2);
      Vector3 vectorDirection = new Vector3(0, -1, 0);

      Ray ray = new Ray(vectorOrigin, vectorDirection);

      Intersection state = plane.GetHitInfo(ray);

      Point3 expectedHitPoint = new Point3(2, 0, 2);

      Assert.assertTrue(state.hits);
      Assert.assertEquals(state.location, expectedHitPoint);
   }

   @Test
       public void testHitInfo2() throws Exception {

      Point3 planeOrigin = new Point3(0, 0, 0);
      Normal normal = new Normal(0, 0, 1);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point3 vectorOrigin = new Point3(500, 500, 500);
      Vector3 vectorDirection = new Vector3(0, 0, -1);

      Ray ray = new Ray(vectorOrigin, vectorDirection);

      Intersection state = plane.GetHitInfo(ray);

      Point3 expectedHitPoint = new Point3(500, 500, 0);

      Assert.assertTrue(state.hits);
      Assert.assertEquals(state.location, expectedHitPoint);
   }

   @Test
   public void testHitInfo3() throws Exception {

      Point3 planeOrigin = new Point3(1, 0, 0);
      Normal normal = new Normal(1, 0, 1);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point3 vectorOrigin = new Point3(500, 500, 500);
      Vector3 vectorDirection = new Vector3(0, 0, -1);

      Ray ray = new Ray(vectorOrigin, vectorDirection);

      Intersection state = plane.GetHitInfo(ray);

      Point3 expectedHitPoint = new Point3(500, 500, -499);

      Assert.assertTrue(state.hits);
      Assert.assertEquals(state.location, expectedHitPoint);
   }

   @Test
   public void testHitInfo4() throws Exception {

      Point3 planeOrigin = new Point3(0, 750, 0);
      Normal normal = new Normal(0, -1, 0);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point3 vectorOrigin = new Point3(700, 500, 1600);
      Vector3 vectorDirection = new Vector3(0, 0, -1);

      Ray ray = new Ray(vectorOrigin, vectorDirection);

      Intersection state = plane.GetHitInfo(ray);

      //Point expectedHitPoint = new Point(500, 500, -499);

      Assert.assertFalse(state.hits);
   }

   @Test
   public void testHitInfo5() throws Exception {

      Point3 planeOrigin = new Point3(0, 750, 0);
      Normal normal = new Normal(0, -1, 0);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point3 vectorOrigin = new Point3(700, 500, 1600);
      Vector3 vectorDirection = new Vector3(0, -1, -1);

      Ray ray = new Ray(vectorOrigin, vectorDirection);

      Intersection state = plane.GetHitInfo(ray);

      //Point expectedHitPoint = new Point(500, 500, -499);

      Assert.assertFalse(state.hits);
   }

   @Test
   public void testHitFromBehindCase2() throws Exception {
      Point3 planeOrigin = new Point3(5, 0, 0);
      Normal normal = new Normal(-1, -1, 0);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point3 vectorOrigin = new Point3(1, 10, 0);
      Vector3 vectorDirection = new Vector3(0, -1, 0);

      Ray ray = new Ray(vectorOrigin, vectorDirection);

      Intersection state = plane.GetHitInfo(ray);

      //Point expectedHitPoint = new Point(500, 500, -499);

      Assert.assertTrue(state.hits);
   }
}
