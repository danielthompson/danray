package test.shapes.sphere;

import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.states.IntersectionState;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static java.lang.Math.sqrt;

/**
 * DanRay
 * User: dthompson
 * Date: 7/1/13
 * Time: 11:13 AM
 */
public class NormalTests {

   public double delta = .0000000000001;

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testGetNormal1() throws Exception{
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(1, 0, 0);
      state.Normal = new Normal(1, 0, 0);
      state.Hits = true;

      Normal normal = state.Normal;
      Normal expectedNormal = new Normal(1, 0, 0);

      Assert.assertEquals(normal.X, expectedNormal.X);
      Assert.assertEquals(normal.Y, expectedNormal.Y);
      Assert.assertEquals(normal.Z, expectedNormal.Z);
   }

   @Test
   public void testGetNormal2() throws Exception{
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(0, 1, 0);
      state.Normal = new Normal(0, 1, 0);
      state.Hits = true;

      Normal normal = state.Normal;

      Normal expectedNormal = new Normal(0, 1, 0);

      Assert.assertEquals(normal.X, expectedNormal.X);
      Assert.assertEquals(normal.Y, expectedNormal.Y);
      Assert.assertEquals(normal.Z, expectedNormal.Z);
   }

   @Test
   public void testGetNormal3() throws Exception{
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(4, 4, 0);
      sphere.Radius = 1;

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(sphere.Origin.X - sphere.Radius / (sqrt(2)), sphere.Origin.Y - sphere.Radius / (sqrt(2)), 0);
      state.Normal = new Normal(- sphere.Radius / (sqrt(2)), - sphere.Radius / (sqrt(2)), 0);
      state.Hits = true;

      Normal normal = state.Normal;
      Normal expectedNormal = new Normal(-1, -1, 0);
      expectedNormal.Normalize();


      Assert.assertEquals(normal.X, expectedNormal.X, delta);
      Assert.assertEquals(normal.Y, expectedNormal.Y, delta);
      Assert.assertEquals(normal.Z, expectedNormal.Z, delta);
   }

   @Test
   public void testGetNormal4() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      IntersectionState state = new IntersectionState();
      state.IntersectionPoint = new Point(1, 0, 0);
      state.Normal = new Normal(1, 0, 0);
      state.Hits = true;

      Normal normal = state.Normal;
      Normal expectedNormal = new Normal(1, 0, 0);

      Assert.assertEquals(expectedNormal.X, normal.X);
      Assert.assertEquals(expectedNormal.Y, normal.Y);
      Assert.assertEquals(expectedNormal.Z, normal.Z);
   }
}
