package test.structures.transform;

import junit.framework.Assert;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector3;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by daniel on 2/16/15.
 */
public class ApplyToPoint3Tests {

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void TestApplyTranslation() {
      Transform t = Transform.translate(new Vector3(1, 1, 1));

      Point3 originalPoint = new Point3(0, 0, 0);

      Point3 actualNewPoint = t.apply(originalPoint);
      Point3 expectedNewPoint = new Point3(1, 1, 1);

      Assert.assertNotNull(actualNewPoint);
      Assert.assertEquals(expectedNewPoint, actualNewPoint);

   }

   @DataProvider(name = "ScaleDataProvider")
   public Object[][] ScaleDataProvider() {
      return new Object[][] {
            { Transform.scale(1, 1, 1), new Point3(0, 0, 0), new Point3(0, 0, 0) },
            { Transform.scale(2, 1, 1), new Point3(0, 0, 0), new Point3(0, 0, 0) },
            { Transform.scale(2, 1, 1), new Point3(1, 1, 1), new Point3(2, 1, 1) },
            { Transform.scale(0, 0, 0), new Point3(1, 1, 1), new Point3(0, 0, 0) },
      };
   }

   @Test(dataProvider = "ScaleDataProvider")
   public void TestApplyScale(Transform transform, Point3 originalPoint, Point3 expectedNewPoint) {

      Point3 actualNewPoint = transform.apply(originalPoint);

      Assert.assertNotNull(actualNewPoint);
      Assert.assertEquals(expectedNewPoint, actualNewPoint);
   }

}
