package test.tracers.tracer;

import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.utility.GeometryCalculations;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.states.Intersection;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * DanRay
 * User: dthompson
 * Date: 7/1/13
 * Time: 9:54 AM
 */
public class PercentageTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testPercentage1() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point3(0, 0, 0);
      sphere.Radius = 1;

      Intersection state = new Intersection();
      state.location = new Point3(1, 0, 0);
      state.Normal = new Normal(1, 0, 0);
      state.Hits = true;

      Point3 vectorOrigin = new Point3(10, 0, 0);
      Vector3 vectorDirection = new Vector3(-1, 0, 0);
      Ray cameraRay = new Ray(vectorOrigin, vectorDirection);

      float actual = GeometryCalculations.GetAngleOfIncidencePercentage(cameraRay, state);
      float expected = 100.0f;
      float delta = Constants.UnitTestDelta * expected;
      Assert.assertEquals(actual, expected, delta);
   }

   @Test
   public void testPercentage2() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point3(0, 0, 0);
      sphere.Radius = 1;

      Intersection state = new Intersection();
      state.location = new Point3(1, 0, 0);
      state.Normal = new Normal(1, 0, 0);
      state.Hits = true;

      Point3 vectorOrigin = new Point3(2, 1, 0);
      Vector3 vectorDirection = new Vector3(-1, -1, 0);
      Ray cameraRay = new Ray(vectorOrigin, vectorDirection);

      float actual = GeometryCalculations.GetAngleOfIncidencePercentage(cameraRay, state);
      float expected = 50.0f;
      float delta = Constants.UnitTestDelta * expected;
      Assert.assertEquals(actual, expected, delta);
   }

   @Test
   public void testPercentage3() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point3(0, 0, 0);
      sphere.Radius = 1;

      Intersection state = new Intersection();
      state.location = new Point3(1, 0, 0);
      state.Normal = new Normal(1, 0, 0);
      state.Hits = true;

      Point3 vectorOrigin = new Point3(1, 1, 0);
      Vector3 vectorDirection = new Vector3(0, -1, 0);
      Ray cameraRay = new Ray(vectorOrigin, vectorDirection);

      float actual = GeometryCalculations.GetAngleOfIncidencePercentage(cameraRay, state);
      float expected = 0.0f;
      float delta = Constants.UnitTestDelta;
      Assert.assertEquals(actual, expected, delta);
   }
}
