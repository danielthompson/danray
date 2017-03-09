package test.tracers.tracer;

import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.utility.GeometryCalculations;
import net.danielthompson.danray.integrators.WhittedIntegrator;
import net.danielthompson.danray.shapes.ImplicitPlane;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.states.IntersectionState;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * DanRay
 * User: dthompson
 * Date: 7/1/13
 * Time: 12:12 PM
 */
public class AngleOfIncidenceTests {

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testAngleOfIncidence1() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point rayOrigin = new Point(2, 1, 0);
      Vector rayDirection = new Vector(-1, -1, 0);

      Ray incomingRay = new Ray(rayOrigin, rayDirection);

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(1, 0, 0);
      state.Normal = new Normal(1, 0, 0);
      state.Hits = true;

      float actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      float expectedAngleOfIncidence = 45;
      float delta = expectedAngleOfIncidence * Constants.UnitTestDelta;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence, delta);

      float actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      float expectedAngleOfIncidencePercentage = 50;

      delta = expectedAngleOfIncidencePercentage * Constants.UnitTestDelta;

      Assert.assertEquals(actualAngleOfIncidencePercentage, expectedAngleOfIncidencePercentage, delta);
   }

   @Test
   public void testAngleOfIncidence2() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point rayOrigin = new Point(2, -1, 0);
      Vector rayDirection = new Vector(-1, 1, 0);

      Ray incomingRay = new Ray(rayOrigin, rayDirection);

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(1, 0, 0);
      state.Normal = new Normal(1, 0, 0);
      state.Hits = true;

      float actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      float expectedAngleOfIncidence = 45;
      float delta = expectedAngleOfIncidence * Constants.UnitTestDelta;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence, delta);

      float actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      float expectedAngleOfIncidencePercentage = 50;
      delta = expectedAngleOfIncidencePercentage * Constants.UnitTestDelta;

      Assert.assertEquals(actualAngleOfIncidencePercentage, expectedAngleOfIncidencePercentage, delta);
   }

   @Test
   public void testAngleOfIncidence3() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point rayOrigin = new Point(1 + Constants.Root3, -1, 0);
      Vector rayDirection = new Vector(-Constants.Root3, 1, 0);

      Ray incomingRay = new Ray(rayOrigin, rayDirection);

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(1, 0, 0);
      state.Normal = new Normal(1, 0, 0);
      state.Hits = true;

      float actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      float expectedAngleOfIncidence = 30;
      float delta = expectedAngleOfIncidence * Constants.UnitTestDelta;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence, delta);

      float actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      float expectedAngleOfIncidencePercentage = 66.0f + 2.0f / 3.0f;
      delta = expectedAngleOfIncidencePercentage * Constants.UnitTestDelta;

      Assert.assertEquals(actualAngleOfIncidencePercentage, expectedAngleOfIncidencePercentage, delta);
   }

   @Test
   public void testAngleOfIncidence4() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point rayOrigin = new Point(4, 0, 0);
      Vector rayDirection = new Vector(-1, 0, 0);

      Ray incomingRay = new Ray(rayOrigin, rayDirection);

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(1, 0, 0);
      state.Normal = new Normal(1, 0, 0);
      state.Hits = true;

      WhittedIntegrator tracer = new WhittedIntegrator(null, 0);

      float actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      float expectedAngleOfIncidence = 0;
      float delta = expectedAngleOfIncidence * Constants.UnitTestDelta;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence, delta);

      float actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      float expectedAngleOfIncidencePercentage = 100;
      delta = expectedAngleOfIncidencePercentage * Constants.UnitTestDelta;

      Assert.assertEquals(actualAngleOfIncidencePercentage, expectedAngleOfIncidencePercentage, delta);
   }

   @Test
   public void testAngleOfIncidence5() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point rayOrigin = new Point(2, -Constants.Root3, 0);
      Vector rayDirection = new Vector(-1, Constants.Root3, 0);

      WhittedIntegrator tracer = new WhittedIntegrator(null, 0);

      Ray incomingRay = new Ray(rayOrigin, rayDirection);

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(1, 0, 0);
      state.Normal = new Normal(1, 0, 0);
      state.Hits = true;

      float actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      float expectedAngleOfIncidence = 60;
      float delta = expectedAngleOfIncidence * Constants.UnitTestDelta;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence, delta);

      float actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      float expectedAngleOfIncidencePercentage = 33.0f + 1.0f / 3.0f;
      delta = expectedAngleOfIncidencePercentage * Constants.UnitTestDelta;

      Assert.assertEquals(actualAngleOfIncidencePercentage, expectedAngleOfIncidencePercentage, delta);
   }

   @Test
   public void testAngleOfIncidenceGlancing1() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point rayOrigin = new Point(2, 1, 0);
      Vector rayDirection = new Vector(-1, 0, 0);

      Ray incomingRay = new Ray(rayOrigin, rayDirection);

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(0, 1, 0);
      state.Normal = new Normal(0, 1, 0);
      state.Hits = true;

      WhittedIntegrator tracer = new WhittedIntegrator(null, 0);

      float actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      float expectedAngleOfIncidence = 90;
      float delta = expectedAngleOfIncidence * Constants.UnitTestDelta;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence, delta);

      float actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      float expectedAngleOfIncidencePercentage = 0;
      delta = actualAngleOfIncidencePercentage * Constants.UnitTestDelta;

      Assert.assertEquals(actualAngleOfIncidencePercentage, expectedAngleOfIncidencePercentage, Constants.UnitTestDelta);
   }

   @Test
   public void testAngleOfIncidenceOpposite() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point rayOrigin = new Point(-.1f, -.9f, 0);
      Vector rayDirection = new Vector(1, -1, 0);

      Ray incomingRay = new Ray(rayOrigin, rayDirection);

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(0, -1, 0);
      state.Normal = new Normal(0, -1, 0);
      state.Hits = true;

      float actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      float expectedAngleOfIncidence = 135;
      float delta = expectedAngleOfIncidence * Constants.UnitTestDelta;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence, delta);

      float actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      float expectedAngleOfIncidencePercentage = 50;
      delta = expectedAngleOfIncidencePercentage * Constants.UnitTestDelta;

      Assert.assertEquals(actualAngleOfIncidencePercentage, expectedAngleOfIncidencePercentage, delta);
   }

}
