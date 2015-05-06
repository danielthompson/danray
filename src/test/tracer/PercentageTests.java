package test.tracer;

import net.danielthompson.danray.GeometryCalculations;
import net.danielthompson.danray.Tracer;
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
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(1, 0, 0);
      state.Hits = true;

      Point vectorOrigin = new Point (10, 0, 0);
      Vector vectorDirection = new Vector(-1, 0, 0);
      Ray cameraRay = new Ray(vectorOrigin, vectorDirection);

      Tracer tracer = new Tracer(null, 0);

      double percentage = GeometryCalculations.GetAngleOfIncidencePercentage(cameraRay, state);
      Assert.assertEquals(percentage, 100.0);
   }

   @Test
   public void testPercentage2() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(1, 0, 0);
      state.Hits = true;

      Point vectorOrigin = new Point (2, 1, 0);
      Vector vectorDirection = new Vector(-1, -1, 0);
      Ray cameraRay = new Ray(vectorOrigin, vectorDirection);

      Tracer tracer = new Tracer(null, 0);

      double percentage = GeometryCalculations.GetAngleOfIncidencePercentage(cameraRay, state);
      Assert.assertEquals(percentage, 50.0);
   }

   @Test
   public void testPercentage3() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(1, 0, 0);
      state.Hits = true;

      Point vectorOrigin = new Point (1, 1, 0);
      Vector vectorDirection = new Vector(0, -1, 0);
      Ray cameraRay = new Ray(vectorOrigin, vectorDirection);

      Tracer tracer = new Tracer(null, 0);

      double percentage = GeometryCalculations.GetAngleOfIncidencePercentage(cameraRay, state);
      Assert.assertEquals(percentage, 0.0);
   }
}
