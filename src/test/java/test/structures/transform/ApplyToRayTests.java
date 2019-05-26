package test.structures.transform;

import junit.framework.Assert;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector3;
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
                  new Ray(new Point3(0, 0, 0), new Vector3(1, 1, 1)),
                  Transform.scale(1, 1, 1),
                  new Ray(new Point3(0, 0, 0), new Vector3(1, 1, 1)) },
            {
                  new Ray(new Point3(0, 0, 0), new Vector3(1, 1, 1)),
                  Transform.scale(-1, -1, -1),
                  new Ray(new Point3(0, 0, 0), new Vector3(-1, -1, -1)) },
            {
                  new Ray(new Point3(2, 2, 2), new Vector3(1, 1, 1)),
                  Transform.scale(1, 1, 1),
                  new Ray(new Point3(2, 2, 2), new Vector3(1, 1, 1)) },
            {
                  new Ray(new Point3(2, 2, 2), new Vector3(5, 5, 5)),
                  Transform.scale(-1, 5, 1),
                  new Ray(new Point3(-2, 10, 2), new Vector3(-5, 25, 5)) },
      };
   }

   @Test(dataProvider = "ScaleDataProvider")
   public void TestApplyScale(Ray originalRay, Transform transform, Ray expected) {

      Ray actual = transform.apply(originalRay);

      Assert.assertNotNull(actual);
      AssertHelper.assertEquals(actual, expected);
   }
}
