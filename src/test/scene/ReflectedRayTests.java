package scene;

import org.testng.annotations.*;

/**
 * User: daniel
 * Date: 7/5/13
 * Time: 20:07
 */
public class ReflectedRayTests {

   public double delta = .0000000000001;

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

      Assert.assertEquals(actual.Origin.X, expected.Origin.X, delta, "Actual: " + actual + " Expected: " + expected);
      Assert.assertEquals(actual.Origin.Y, expected.Origin.Y, delta, "Actual: " + actual + " Expected: " + expected);
      Assert.assertEquals(actual.Origin.Z, expected.Origin.Z, delta, "Actual: " + actual + " Expected: " + expected);

      Assert.assertEquals(actual.Direction.X, expected.Direction.X, delta, "Actual: " + actual + " Expected: " + expected);
      Assert.assertEquals(actual.Direction.Y, expected.Direction.Y, delta, "Actual: " + actual + " Expected: " + expected);
      Assert.assertEquals(actual.Direction.Z, expected.Direction.Z, delta, "Actual: " + actual + " Expected: " + expected);
   }

   @Test
   public void testReflectedRay2() throws Exception {

      double root3 = Math.sqrt(3);

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

      Assert.assertEquals(actual.Origin.X, expected.Origin.X, delta, "Actual: " + actual + " Expected: " + expected);
      Assert.assertEquals(actual.Origin.Y, expected.Origin.Y, delta, "Actual: " + actual + " Expected: " + expected);
      Assert.assertEquals(actual.Origin.Z, expected.Origin.Z, delta, "Actual: " + actual + " Expected: " + expected);

      Assert.assertEquals(actual.Direction.X, expected.Direction.X, delta, "Actual: " + actual + " Expected: " + expected);
      Assert.assertEquals(actual.Direction.Y, expected.Direction.Y, delta, "Actual: " + actual + " Expected: " + expected);
      Assert.assertEquals(actual.Direction.Z, expected.Direction.Z, delta, "Actual: " + actual + " Expected: " + expected);
   }

   @Test
   public void testReflectedRay3() throws Exception {

      double root3 = Math.sqrt(3);

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

      Assert.assertEquals(actual.Origin.X, expected.Origin.X, delta, "Actual: " + actual + " Expected: " + expected);
      Assert.assertEquals(actual.Origin.Y, expected.Origin.Y, delta, "Actual: " + actual + " Expected: " + expected);
      Assert.assertEquals(actual.Origin.Z, expected.Origin.Z, delta, "Actual: " + actual + " Expected: " + expected);

      Assert.assertEquals(actual.Direction.X, expected.Direction.X, delta, "Actual: " + actual + " Expected: " + expected);
      Assert.assertEquals(actual.Direction.Y, expected.Direction.Y, delta, "Actual: " + actual + " Expected: " + expected);
      Assert.assertEquals(actual.Direction.Z, expected.Direction.Z, delta, "Actual: " + actual + " Expected: " + expected);
   }*/
}