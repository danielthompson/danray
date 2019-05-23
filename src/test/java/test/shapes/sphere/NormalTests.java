package test.shapes.sphere;

import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.shapes.Sphere;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * DanRay
 * User: dthompson
 * Date: 7/1/13
 * Time: 11:13 AM
 */
public class NormalTests {

   public float delta = .0000000000001f;

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testGetNormal1() throws Exception{
      Sphere sphere = new Sphere();
      sphere.Origin = new Point3(0, 0, 0);
      sphere.Radius = 1;

      Intersection state = new Intersection();
      state.Location = new Point3(1, 0, 0);
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
      sphere.Origin = new Point3(0, 0, 0);
      sphere.Radius = 1;

      Intersection state = new Intersection();
      state.Location = new Point3(0, 1, 0);
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
      sphere.Origin = new Point3(4, 4, 0);
      sphere.Radius = 1;

      Intersection state = new Intersection();
      state.Location = new Point3(sphere.Origin.x - sphere.Radius / (Constants.Root2), sphere.Origin.y - sphere.Radius / (Constants.Root2), 0);
      state.Normal = new Normal(- sphere.Radius / (Constants.Root2), - sphere.Radius / (Constants.Root2), 0);
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
      sphere.Origin = new Point3(0, 0, 0);
      sphere.Radius = 1;

      Intersection state = new Intersection();
      state.Location = new Point3(1, 0, 0);
      state.Normal = new Normal(1, 0, 0);
      state.Hits = true;

      Normal normal = state.Normal;
      Normal expectedNormal = new Normal(1, 0, 0);

      Assert.assertEquals(expectedNormal.X, normal.X);
      Assert.assertEquals(expectedNormal.Y, normal.Y);
      Assert.assertEquals(expectedNormal.Z, normal.Z);
   }
}
