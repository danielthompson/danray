package test.structures.transform;

import junit.framework.Assert;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.AssertHelper;

/**
 * Created by daniel on 2/16/15.
 */
public class ApplyToRayTests {

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @DataProvider(name = "ScaleDataProvider")
   public Object[][] ScaleDataProvider() {
      return new Object[][] {
            {
                  new Ray(new Point(0, 0, 0), new Vector(1, 1, 1)),
                  Transform.Scale(1, 1, 1),
                  new Ray(new Point(0, 0, 0), new Vector(1, 1, 1)) },
            {
                  new Ray(new Point(0, 0, 0), new Vector(1, 1, 1)),
                  Transform.Scale(-1, -1, -1),
                  new Ray(new Point(0, 0, 0), new Vector(-1, -1, -1)) },
            {
                  new Ray(new Point(2, 2, 2), new Vector(1, 1, 1)),
                  Transform.Scale(1, 1, 1),
                  new Ray(new Point(2, 2, 2), new Vector(1, 1, 1)) },
            {
                  new Ray(new Point(2, 2, 2), new Vector(5, 5, 5)),
                  Transform.Scale(-1, 5, 1),
                  new Ray(new Point(-2, 10, 2), new Vector(-5, 25, 5)) },
      };
   }

   @Test(dataProvider = "ScaleDataProvider")
   public void TestApplyScale(Ray originalRay, Transform transform, Ray expected) {

      Ray actual = transform.Apply(originalRay);

      Assert.assertNotNull(actual);
      AssertHelper.assertEquals(actual, expected);
   }
}
