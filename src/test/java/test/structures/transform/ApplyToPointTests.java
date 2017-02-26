package test.structures.transform;

import junit.framework.Assert;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by daniel on 2/16/15.
 */
public class ApplyToPointTests {

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void TestApplyTranslation() {
      Transform t = Transform.Translate(new Vector(1, 1, 1));

      Point originalPoint = new Point(0, 0, 0);

      Point actualNewPoint = t.Apply(originalPoint);
      Point expectedNewPoint = new Point(1, 1, 1);

      Assert.assertNotNull(actualNewPoint);
      Assert.assertEquals(expectedNewPoint, actualNewPoint);

   }

   @DataProvider(name = "ScaleDataProvider")
   public Object[][] ScaleDataProvider() {
      return new Object[][] {
            { Transform.Scale(1, 1, 1), new Point(0, 0, 0), new Point(0, 0, 0) },
            { Transform.Scale(2, 1, 1), new Point(0, 0, 0), new Point(0, 0, 0) },
            { Transform.Scale(2, 1, 1), new Point(1, 1, 1), new Point(2, 1, 1) },
            { Transform.Scale(0, 0, 0), new Point(1, 1, 1), new Point(0, 0, 0) },
      };
   }

   @Test(dataProvider = "ScaleDataProvider")
   public void TestApplyScale(Transform transform, Point originalPoint, Point expectedNewPoint) {

      Point actualNewPoint = transform.Apply(originalPoint);

      Assert.assertNotNull(actualNewPoint);
      Assert.assertEquals(expectedNewPoint, actualNewPoint);
   }

}
