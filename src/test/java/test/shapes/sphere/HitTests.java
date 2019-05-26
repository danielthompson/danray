package test.shapes.sphere;

import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector3;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 5:13 PM
 */
public class HitTests {

   @DataProvider(name = "HitDataProvider")
   public Object[][] HitDataProvider() {
      return new Object[][]{
            {new Point3(2, 0, 0), new Vector3(1, 0, 0), false},
            {new Point3(-2, 0, 0), new Vector3(1, 0, 0), true},
            {new Point3(-2, 0, 0), new Vector3(-1, 0, 0), false},
            {new Point3(10, 0, 0), new Vector3(1, 0, 0), false},
            {new Point3(0, 10, 0), new Vector3(-1, 0, 0), false},
            {new Point3(0, 10, 0), new Vector3(0, -1, 0), true},
            {new Point3(0, 10, 0), new Vector3(0, 1, 0), false},
            {new Point3(10, 10, 0), new Vector3(-1, -1, 0), true},
            {new Point3(-10, -10, 0), new Vector3(1, 1, 0), true},
            {new Point3(-10, -10, 2), new Vector3(1, 1, 0), false},
            {new Point3(0, -10, 0), new Vector3(0, 1, 0), true},
      };
   }

   @Test(dataProvider = "HitDataProvider")
   public void testHits(Point3 origin, Vector3 direction, boolean hits) {
      Sphere sphere = new Sphere();
      Ray ray = new Ray(origin, direction);

      boolean actualHits = sphere.hits(ray);

      String text = "Vector shouldn't hit sphere";
      if (hits) {
         text = "Vector should hit sphere";
      }

      Assert.assertEquals(actualHits, hits, text);
   }
}
