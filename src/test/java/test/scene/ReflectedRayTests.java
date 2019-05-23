package test.scene;

import org.testng.annotations.*;

/**
 * User: daniel
 * Date: 7/5/13
 * Time: 20:07
 */
public class ReflectedRayTests {

   public float delta = .0000000000001f;

   @org.testng.annotations.BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }
/*
   @Test
   public void testReflectedRay1() throws Exception {

      Point normalOrigin = new Point(1, 0, 0);
      Point normalDirection = new Point(1, 0, 0);
      Vector normal = new Vector(normalOrigin, normalDirection);

      Point incomingOrigin = new Point(2, 1, 0);
      Point incomingDirection = new Point(-1, -1, 0);
      Vector incomingRay = new Vector(incomingOrigin, incomingDirection);

      Point expectedOrigin = normalOrigin;
      Point expectedDirection = new Point (1, -1, 0);
      Vector expected = new Vector(expectedOrigin, expectedDirection);

      Scene scene = new Scene(null);

      Vector actual =  scene.GetReflectedRay(normal, incomingRay);

      AssertHelper.assertEquals(actual.Origin.x, expected.Origin.x, delta, "Actual: " + actual + " Expected: " + expected);
      AssertHelper.assertEquals(actual.Origin.y, expected.Origin.y, delta, "Actual: " + actual + " Expected: " + expected);
      AssertHelper.assertEquals(actual.Origin.z, expected.Origin.z, delta, "Actual: " + actual + " Expected: " + expected);

      AssertHelper.assertEquals(actual.Direction.x, expected.Direction.x, delta, "Actual: " + actual + " Expected: " + expected);
      AssertHelper.assertEquals(actual.Direction.y, expected.Direction.y, delta, "Actual: " + actual + " Expected: " + expected);
      AssertHelper.assertEquals(actual.Direction.z, expected.Direction.z, delta, "Actual: " + actual + " Expected: " + expected);
   }

   @Test
   public void testReflectedRay2() throws Exception {

      float root3 = Math.sqrt(3);

      Point normalOrigin = new Point(1, 0, 0);
      Point normalDirection = new Point(1, 0, 0);
      Vector normal = new Vector(normalOrigin, normalDirection);

      Point incomingOrigin = new Point(2, root3, 0);
      Point incomingDirection = new Point(-1, -root3, 0);
      Vector incomingRay = new Vector(incomingOrigin, incomingDirection);

      Point expectedOrigin = normalOrigin;
      Point expectedDirection = new Point (1, -root3, 0);
      Vector expected = new Vector(expectedOrigin, expectedDirection);

      Scene scene = new Scene(null);

      Vector actual =  scene.GetReflectedRay(normal, incomingRay);

      AssertHelper.assertEquals(actual.Origin.x, expected.Origin.x, delta, "Actual: " + actual + " Expected: " + expected);
      AssertHelper.assertEquals(actual.Origin.y, expected.Origin.y, delta, "Actual: " + actual + " Expected: " + expected);
      AssertHelper.assertEquals(actual.Origin.z, expected.Origin.z, delta, "Actual: " + actual + " Expected: " + expected);

      AssertHelper.assertEquals(actual.Direction.x, expected.Direction.x, delta, "Actual: " + actual + " Expected: " + expected);
      AssertHelper.assertEquals(actual.Direction.y, expected.Direction.y, delta, "Actual: " + actual + " Expected: " + expected);
      AssertHelper.assertEquals(actual.Direction.z, expected.Direction.z, delta, "Actual: " + actual + " Expected: " + expected);
   }

   @Test
   public void testReflectedRay3() throws Exception {

      float root3 = Math.sqrt(3);

      Point normalOrigin = new Point(0, 1, 0);
      Point normalDirection = new Point(0, 1, 0);
      Vector normal = new Vector(normalOrigin, normalDirection);

      Point incomingOrigin = new Point(-1, 1 + root3, 0);
      Point incomingDirection = new Point(1, -root3, 0);
      Vector incomingRay = new Vector(incomingOrigin, incomingDirection);

      Point expectedOrigin = normalOrigin;
      Point expectedDirection = new Point (1, root3, 0);
      Vector expected = new Vector(expectedOrigin, expectedDirection);

      Scene scene = new Scene(null);

      Vector actual =  scene.GetReflectedRay(normal, incomingRay);

      AssertHelper.assertEquals(actual.Origin.x, expected.Origin.x, delta, "Actual: " + actual + " Expected: " + expected);
      AssertHelper.assertEquals(actual.Origin.y, expected.Origin.y, delta, "Actual: " + actual + " Expected: " + expected);
      AssertHelper.assertEquals(actual.Origin.z, expected.Origin.z, delta, "Actual: " + actual + " Expected: " + expected);

      AssertHelper.assertEquals(actual.Direction.x, expected.Direction.x, delta, "Actual: " + actual + " Expected: " + expected);
      AssertHelper.assertEquals(actual.Direction.y, expected.Direction.y, delta, "Actual: " + actual + " Expected: " + expected);
      AssertHelper.assertEquals(actual.Direction.z, expected.Direction.z, delta, "Actual: " + actual + " Expected: " + expected);
   }*/
}