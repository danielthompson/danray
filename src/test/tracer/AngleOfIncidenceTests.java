package test.tracer;

import net.danielthompson.danray.GeometryCalculations;
import net.danielthompson.danray.Tracer;
import net.danielthompson.danray.shapes.ImplicitPlane;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.Vector;
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
   public double delta = .0000000000001;

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
      state.Hits = true;

      Tracer tracer = new Tracer(null, 0);

      double actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      double expectedAngleOfIncidence = 45;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence);

      double actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      double expectedAngleOfIncidencePercentage = 50;

      Assert.assertEquals(actualAngleOfIncidencePercentage, expectedAngleOfIncidencePercentage);
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
      state.Hits = true;

      Tracer tracer = new Tracer(null, 0);

      double actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      double expectedAngleOfIncidence = 45;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence);

      double actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      double expectedAngleOfIncidencePercentage = 50;

      Assert.assertEquals(actualAngleOfIncidencePercentage, expectedAngleOfIncidencePercentage);
   }

   @Test
   public void testAngleOfIncidence3() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      double root3 = Math.sqrt(3);

      Point rayOrigin = new Point(1 + root3, -1, 0);
      Vector rayDirection = new Vector(-root3, 1, 0);

      Ray incomingRay = new Ray(rayOrigin, rayDirection);

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(1, 0, 0);
      state.Hits = true;

      Tracer tracer = new Tracer(null, 0);

      double actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      double expectedAngleOfIncidence = 30;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence, delta);

      double actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      double expectedAngleOfIncidencePercentage = 66.0 + 2.0 / 3.0;

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
      state.Hits = true;

      Tracer tracer = new Tracer(null, 0);

      double actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      double expectedAngleOfIncidence = 0;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence);

      double actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      double expectedAngleOfIncidencePercentage = 100;

      Assert.assertEquals(actualAngleOfIncidencePercentage, expectedAngleOfIncidencePercentage, delta);
   }

   @Test
   public void testAngleOfIncidence5() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      double root3 = Math.sqrt(3);

      Point rayOrigin = new Point(2, -root3, 0);
      Vector rayDirection = new Vector(-1, root3, 0);

      Tracer tracer = new Tracer(null, 0);

      Ray incomingRay = new Ray(rayOrigin, rayDirection);

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(1, 0, 0);
      state.Hits = true;

      double actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      double expectedAngleOfIncidence = 60;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence, delta);

      double actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      double expectedAngleOfIncidencePercentage = 33.0 + 1.0 / 3.0;

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
      state.Hits = true;

      Tracer tracer = new Tracer(null, 0);

      double actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      double expectedAngleOfIncidence = 90;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence);

      double actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      double expectedAngleOfIncidencePercentage = 0;

      Assert.assertEquals(actualAngleOfIncidencePercentage, expectedAngleOfIncidencePercentage, delta);
   }

   @Test
   public void testAngleOfIncidenceOpposite() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point rayOrigin = new Point(0, -1, 0);
      Vector rayDirection = new Vector(1, 1, 0);

      Ray incomingRay = new Ray(rayOrigin, rayDirection);

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(0, 1, 0);
      state.Hits = true;

      Tracer tracer = new Tracer(null, 0);

      double actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      double expectedAngleOfIncidence = 135;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence);

      double actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      double expectedAngleOfIncidencePercentage = -50;

      Assert.assertEquals(actualAngleOfIncidencePercentage, expectedAngleOfIncidencePercentage, delta);
   }

   @Test
   public void testAngleOfIncidenceOpposite2() throws Exception {
      Point planeOrigin = new Point(0, 0, 0);
      Normal normal = new Normal(0, 1, 0);


      ImplicitPlane plane = new ImplicitPlane(planeOrigin, normal, null);

      Point vectorOrigin = new Point(2, 2, 2);
      Vector vectorDirection = new Vector(0, -1, 0);

      Ray incomingRay = new Ray(vectorOrigin, vectorDirection);

      IntersectionState state = plane.GetHitInfo(incomingRay);

      Point expectedHitPoint = new Point(2, 0, 2);

      Assert.assertTrue(state.Hits);
      Assert.assertEquals(state.IntersectionPoint, expectedHitPoint);

      Tracer tracer = new Tracer(null, 0);

      double actualAngleOfIncidence = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);
      double expectedAngleOfIncidence = 135;

      Assert.assertEquals(actualAngleOfIncidence, expectedAngleOfIncidence);

      double actualAngleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(incomingRay, state);
      double expectedAngleOfIncidencePercentage = -50;

      Assert.assertEquals(actualAngleOfIncidencePercentage, expectedAngleOfIncidencePercentage, delta);
   }
}
