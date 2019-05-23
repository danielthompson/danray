package test.structures.boundingbox;

import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point3;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.AssertHelper;

/**
 * DanRay
 * User: dthompson
 * Date: 7/24/13
 * Time: 1:52 PM
 */
public class GetBoundingBoxTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testGetBoundingBox1() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point3(0, 0, 0);
      sphere.Radius = 10;
      sphere.RecalculateWorldBoundingBox();

      BoundingBox actualBoundingBox = sphere.WorldBoundingBox;

      Point3 p1 = new Point3(-10, -10, -10);
      Point3 p2 = new Point3(10, 10, 10);
      BoundingBox expectedBoundingBox = new BoundingBox(p1, p2);

      AssertHelper.assertEquals(expectedBoundingBox, actualBoundingBox);
   }

   @Test
   public void testGetBoundingBox2() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point3(1, 1, 1);
      sphere.Radius = 10;
      sphere.RecalculateWorldBoundingBox();

      BoundingBox actualBoundingBox = sphere.WorldBoundingBox;

      Point3 p1 = new Point3(-9, -9, -9);
      Point3 p2 = new Point3(11, 11, 11);
      BoundingBox expectedBoundingBox = new BoundingBox(p1, p2);

      AssertHelper.assertEquals(expectedBoundingBox, actualBoundingBox);
   }
}
