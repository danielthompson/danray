package test.structures.transform;

import junit.framework.Assert;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by daniel on 2/16/15.
 */
public class ApplyToVectorTests {

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void TestApplyTranslation() {
      Transform t = Transform.Translate(new Vector(1, 1, 1));

      Vector originalVector = new Vector(5, 5, 5);

      Vector actualNewVector = t.Apply(originalVector);
      Vector expectedNewVector = new Vector(5, 5, 5);

      Assert.assertNotNull("transformed vector should not be null", actualNewVector);
      Assert.assertEquals(expectedNewVector, actualNewVector);
   }

   @DataProvider(name = "ScaleDataProvider")
   public Object[][] ScaleDataProvider() {
      return new Object[][] {
            { Transform.Scale(1, 1, 1), new Vector(0, 0, 0), new Vector(0, 0, 0) },
            { Transform.Scale(1, 1, 1), new Vector(1, 1, 1), new Vector(1, 1, 1) },
            { Transform.Scale(2, 1, 1), new Vector(1, 1, 1), new Vector(2, 1, 1) },
            { Transform.Scale(0, 1, 1), new Vector(1, 1, 1), new Vector(0, 1, 1) },
      };
   }

   @Test(dataProvider = "ScaleDataProvider")
   public void TestApplyScale(Transform transform, Vector originalVector, Vector expectedNewVector) {

      Vector actualNewVector = transform.Apply(originalVector);

      Assert.assertNotNull(actualNewVector);
      Assert.assertEquals(expectedNewVector, actualNewVector);
   }


}
