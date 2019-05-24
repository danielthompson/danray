package test.structures.boundingbox;

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
   public void shouldHit1() throws Exception {

      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(2, 2, 2);

      BoundingBox box = new BoundingBox(p1, p2);

      Point3 origin = new Point3(1, 1, 10);
      Vector3 direction = new Vector3(0, 0, -1);

      Ray ray = new Ray(origin, direction);

      Intersection state = box.getHitInfo(ray);

      Assert.assertTrue(state.hits, "Should hit");
   }

   @Test
   public void shouldHit2() throws Exception {

      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(2, 2, 2);

      BoundingBox box = new BoundingBox(p1, p2);

      Point3 origin = new Point3(0, 1, 3);
      Vector3 direction = new Vector3(1, 0, -1);

      Ray ray = new Ray(origin, direction);

      Intersection state = box.getHitInfo(ray);

      Assert.assertTrue(state.hits, "Should hit");
   }

   @Test
   public void shouldHit3() throws Exception {

      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(2, 2, 2);

      BoundingBox box = new BoundingBox(p1, p2);

      Point3 origin = new Point3(4, 1, 4);
      Vector3 direction = new Vector3(-1, 0, -1);

      Ray ray = new Ray(origin, direction);

      Intersection state = box.getHitInfo(ray);

      Assert.assertNotNull(state, "intersection shouldn't be null");
      Assert.assertTrue(state.hits, "Should hit");
   }

   @Test
   public void shouldHit4() throws Exception {

      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(2, 2, 2);

      BoundingBox box = new BoundingBox(p1, p2);

      Point3 origin = new Point3(5, 1, 2);
      Vector3 direction = new Vector3(-1, 0, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = box.getHitInfo(ray);


      Assert.assertNotNull(state, "intersection shouldn't be null");
      Assert.assertTrue(state.hits, "Should hit");
   }

   @Test
   public void shouldHit5() throws Exception {

      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(6, 6, 6);

      BoundingBox box = new BoundingBox(p1, p2);

      Point3 origin = new Point3(3, 3, 7);

      for (float x = 0; x < 1; x += .025) {
         for (float y = 0; y < 1; y += .025) {
            Vector3 direction = new Vector3(x, y, -.5f);

            Ray ray = new Ray(origin, direction);

            Intersection state = box.getHitInfo(ray);

            Assert.assertTrue(state.hits, "Should hit");
         }
      }
   }

   @Test
   public void shouldHitFromInside1() throws Exception {

      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(2, 2, 2);

      BoundingBox box = new BoundingBox(p1, p2);

      Point3 origin = new Point3(1, 1, 1);
      Vector3 direction = new Vector3(1, 0, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = box.getHitInfo(ray);

      //Vector expectedNormalDirection = new Vector(-1, 0, 0);

      Assert.assertTrue(state.hits, "Should hit");
      //Assert.assertEquals(state.normal, expectedNormalDirection, "normal direction fails");
   }

   @Test
   public void testShouldntHit1() throws Exception {

      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(2, 2, 2);

      BoundingBox box = new BoundingBox(p1, p2);

      Point3 origin = new Point3(-1, -1, 10);
      Vector3 direction = new Vector3(0, 0, -1);

      Ray ray = new Ray(origin, direction);

      Intersection state = box.getHitInfo(ray);


      Assert.assertFalse(state.hits, "Shouldn't hit");
      Assert.assertNull(state.location, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.normal, "Shouldn't be a normal for no hit");
   }

   @Test
   public void testShouldntHit2() throws Exception {

      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(2, 2, 2);

      BoundingBox box = new BoundingBox(p1, p2);

      Point3 origin = new Point3(-5, 1, 4);
      Vector3 direction = new Vector3(1, 0, -1);

      Ray ray = new Ray(origin, direction);

      Intersection state = box.getHitInfo(ray);

      Assert.assertFalse(state.hits, "Shouldn't hit");
      Assert.assertNull(state.location, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.normal, "Shouldn't be a normal for no hit");
   }

   @Test
   public void testShouldntHit3() throws Exception {

      // box
      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(2, 2, 2);
      BoundingBox box = new BoundingBox(p1, p2);

      // vector
      Point3 origin = new Point3(-1, 1, 3);
      Vector3 direction = new Vector3(-1, 0, 1);
      Ray ray = new Ray(origin, direction);

      Intersection state = box.getHitInfo(ray);

      Assert.assertFalse(state.hits, "Shouldn't hit");
      Assert.assertNull(state.location, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.normal, "Shouldn't be a normal for no hit");
   }

   @Test
   public void testShouldntHit4() throws Exception {

      // box
      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(2, 2, 2);
      BoundingBox box = new BoundingBox(p1, p2);

      // vector
      Point3 origin = new Point3(-1, 1, 3);
      Vector3 direction = new Vector3(-1, 1, 1);
      Ray ray = new Ray(origin, direction);

      Intersection state = box.getHitInfo(ray);

      Assert.assertFalse(state.hits, "Shouldn't hit");
      Assert.assertNull(state.location, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.normal, "Shouldn't be a normal for no hit");
   }

   @Test
   public void testShouldntHit5() throws Exception {

      // box
      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(2, 2, 2);
      BoundingBox box = new BoundingBox(p1, p2);

      // vector
      Point3 origin = new Point3(3, 1, 7);
      Vector3 direction = new Vector3(1, 0, -1);
      Ray ray = new Ray(origin, direction);

      Intersection state = box.getHitInfo(ray);

      Assert.assertFalse(state.hits, "Shouldn't hit");
      Assert.assertNull(state.location, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.normal, "Shouldn't be a normal for no hit");
   }

   @Test
   public void testShouldntHit6() throws Exception {

      // box
      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(2, 2, 2);
      BoundingBox box = new BoundingBox(p1, p2);

      // vector
      Point3 origin = new Point3(7, 5, 5);
      Vector3 direction = new Vector3(0, -1, -1);
      Ray ray = new Ray(origin, direction);

      Intersection state = box.getHitInfo(ray);

      Assert.assertFalse(state.hits, "Shouldn't hit");
      Assert.assertNull(state.location, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.normal, "Shouldn't be a normal for no hit");
   }

   @Test
   public void testShouldntHit7() throws Exception {

      // box
      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(2, 2, 2);
      BoundingBox box = new BoundingBox(p1, p2);

      // vector

      for (int i = -50; i < 50; i++) {
         for (float j = -1; j < 1; j += .025) {
            for (float k = -1; k < 1; k += .025) {
               for (float l = -500; l < 500; l += 14.4736) {
                  Point3 origin = new Point3(7, i, l);
                  Vector3 direction = new Vector3(0, j, k);
                  Ray ray = new Ray(origin, direction);

                  Intersection state = box.getHitInfo(ray);

                  Assert.assertFalse(state.hits, "Shouldn't hit");
                  Assert.assertNull(state.location, "Shouldn't be an intersection point if the ray doesn't hit");
                  Assert.assertNull(state.normal, "Shouldn't be a normal for no hit");
               }
            }
         }
      }
   }

   @Test
   public void testShouldntHit8() throws Exception {

      // box
      Point3 p1 = new Point3(0, 0, 0);
      Point3 p2 = new Point3(4, 4, 4);
      BoundingBox box = new BoundingBox(p1, p2);

      // vector
      Point3 origin = new Point3(20, -6, 6);
      Vector3 direction = new Vector3(-1, 1, -1);
      Ray ray = new Ray(origin, direction);

      Intersection state = box.getHitInfo(ray);

      Assert.assertFalse(state.hits, "Shouldn't hit");
      Assert.assertNull(state.location, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.normal, "Shouldn't be a normal for no hit");
   }

   @Test
   public void testShouldntHit9() throws Exception {

      // box
      Point3 p1 = new Point3(250, 550, 250);
      Point3 p2 = new Point3(350, 650, 350);
      BoundingBox box = new BoundingBox(p1, p2);

      // vector
      Point3 origin = new Point3(40.5911474f, 540.5911474f, 794.088525f);
      Vector3 direction = new Vector3(.711770f, .021852f, -.702072f);
      Ray ray = new Ray(origin, direction);

      Intersection state = box.getHitInfo(ray);

      Assert.assertFalse(state.hits, "Shouldn't hit");
      Assert.assertNull(state.location, "Shouldn't be an intersection point if the ray doesn't hit");
      Assert.assertNull(state.normal, "Shouldn't be a normal for no hit");
   }
}
