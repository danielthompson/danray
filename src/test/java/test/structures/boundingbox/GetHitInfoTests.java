package test.structures.boundingbox;

import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.AssertHelper;

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
   public void shouldHit1() throws Exception {

      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);

      BoundingBox box = new BoundingBox(p1, p2);

      Point origin = new Point(1, 1, 10);
      Vector direction = new Vector(0, 0, -1);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      Assert.assertTrue(state.Hits, "Should hit");
   }

   @Test
   public void shouldHit2() throws Exception {

      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);

      BoundingBox box = new BoundingBox(p1, p2);

      Point origin = new Point(0, 1, 3);
      Vector direction = new Vector(1, 0, -1);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      Assert.assertTrue(state.Hits, "Should hit");
   }

   @Test
   public void shouldHit3() throws Exception {

      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);

      BoundingBox box = new BoundingBox(p1, p2);

      Point origin = new Point(4, 1, 4);
      Vector direction = new Vector(-1, 0, -1);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      Assert.assertNotNull(state, "IntersectionState shouldn't be null");
      Assert.assertTrue(state.Hits, "Should hit");
   }

   @Test
   public void shouldHit4() throws Exception {

      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);

      BoundingBox box = new BoundingBox(p1, p2);

      Point origin = new Point(5, 1, 2);
      Vector direction = new Vector(-1, 0, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);


      Assert.assertNotNull(state, "IntersectionState shouldn't be null");
      Assert.assertTrue(state.Hits, "Should hit");
   }

   @Test
   public void shouldHit5() throws Exception {

      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(6, 6, 6);

      BoundingBox box = new BoundingBox(p1, p2);

      Point origin = new Point(3, 3, 7);

      for (double x = 0; x < 1; x += .025) {
         for (double y = 0; y < 1; y += .025) {
            Vector direction = new Vector(x, y, -.5);

            Ray ray = new Ray(origin, direction);

            IntersectionState state = box.GetHitInfo(ray);

            Assert.assertTrue(state.Hits, "Should hit");
         }
      }
   }

   @Test
   public void shouldHitFromInside1() throws Exception {

      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);

      BoundingBox box = new BoundingBox(p1, p2);

      Point origin = new Point(1, 1, 1);
      Vector direction = new Vector(1, 0, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      //Vector expectedNormalDirection = new Vector(-1, 0, 0);

      Assert.assertTrue(state.Hits, "Should hit");
      //Assert.assertEquals(state.Normal, expectedNormalDirection, "normal direction fails");
   }

   @Test
   public void testShouldntHit1() throws Exception {

      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);

      BoundingBox box = new BoundingBox(p1, p2);

      Point origin = new Point(-1, -1, 10);
      Vector direction = new Vector(0, 0, -1);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);


      Assert.assertFalse(state.Hits, "Shouldn't hit");
      Assert.assertNull(state.IntersectionPoint, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.Normal, "Shouldn't be a normal for no hit");
   }

   @Test
   public void testShouldntHit2() throws Exception {

      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);

      BoundingBox box = new BoundingBox(p1, p2);

      Point origin = new Point(-5, 1, 4);
      Vector direction = new Vector(1, 0, -1);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      Assert.assertFalse(state.Hits, "Shouldn't hit");
      Assert.assertNull(state.IntersectionPoint, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.Normal, "Shouldn't be a normal for no hit");
   }

   @Test
   public void testShouldntHit3() throws Exception {

      // box
      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);
      BoundingBox box = new BoundingBox(p1, p2);

      // vector
      Point origin = new Point(-1, 1, 3);
      Vector direction = new Vector(-1, 0, 1);
      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      Assert.assertFalse(state.Hits, "Shouldn't hit");
      Assert.assertNull(state.IntersectionPoint, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.Normal, "Shouldn't be a normal for no hit");
   }

   @Test
   public void testShouldntHit4() throws Exception {

      // box
      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);
      BoundingBox box = new BoundingBox(p1, p2);

      // vector
      Point origin = new Point(-1, 1, 3);
      Vector direction = new Vector(-1, 1, 1);
      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      Assert.assertFalse(state.Hits, "Shouldn't hit");
      Assert.assertNull(state.IntersectionPoint, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.Normal, "Shouldn't be a normal for no hit");
   }

   @Test
   public void testShouldntHit5() throws Exception {

      // box
      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);
      BoundingBox box = new BoundingBox(p1, p2);

      // vector
      Point origin = new Point(3, 1, 7);
      Vector direction = new Vector(1, 0, -1);
      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      Assert.assertFalse(state.Hits, "Shouldn't hit");
      Assert.assertNull(state.IntersectionPoint, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.Normal, "Shouldn't be a normal for no hit");
   }

   @Test
   public void testShouldntHit6() throws Exception {

      // box
      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);
      BoundingBox box = new BoundingBox(p1, p2);

      // vector
      Point origin = new Point(7, 5, 5);
      Vector direction = new Vector(0, -1, -1);
      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      Assert.assertFalse(state.Hits, "Shouldn't hit");
      Assert.assertNull(state.IntersectionPoint, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.Normal, "Shouldn't be a normal for no hit");
   }

   @Test
   public void testShouldntHit7() throws Exception {

      // box
      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);
      BoundingBox box = new BoundingBox(p1, p2);

      // vector

      for (int i = -50; i < 50; i++) {
         for (double j = -1; j < 1; j += .025) {
            for (double k = -1; k < 1; k += .025) {
               for (double l = -500; l < 500; l += 14.4736) {
                  Point origin = new Point(7, i, l);
                  Vector direction = new Vector(0, j, k);
                  Ray ray = new Ray(origin, direction);

                  IntersectionState state = box.GetHitInfo(ray);

                  Assert.assertFalse(state.Hits, "Shouldn't hit");
                  Assert.assertNull(state.IntersectionPoint, "Shouldn't be an intersection point if the ray doesn't hit");
                  Assert.assertNull(state.Normal, "Shouldn't be a normal for no hit");
               }
            }
         }
      }
   }

   @Test
   public void testShouldntHit8() throws Exception {

      // box
      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(4, 4, 4);
      BoundingBox box = new BoundingBox(p1, p2);

      // vector
      Point origin = new Point(20, -6, 6);
      Vector direction = new Vector(-1, 1, -1);
      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      Assert.assertFalse(state.Hits, "Shouldn't hit");
      Assert.assertNull(state.IntersectionPoint, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.Normal, "Shouldn't be a normal for no hit");
   }

   @Test
   public void testShouldntHit9() throws Exception {

      // box
      Point p1 = new Point(250, 550, 250);
      Point p2 = new Point(350, 650, 350);
      BoundingBox box = new BoundingBox(p1, p2);

      // vector
      Point origin = new Point(40.5911474, 540.5911474, 794.088525);
      Vector direction = new Vector(.711770, .021852, -.702072);
      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      Assert.assertFalse(state.Hits, "Shouldn't hit");
      Assert.assertNull(state.IntersectionPoint, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.Normal, "Shouldn't be a normal for no hit");
   }
}
