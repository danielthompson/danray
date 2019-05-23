package test.structures.transform;

import junit.framework.Assert;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by daniel on 2/16/15.
 */
public class LookAtTests {
   Transform t;

   @BeforeMethod
   public void setUp() throws Exception {
      float[][] matrix = new float[4][4];

      matrix[0] = new float[4];
      matrix[1] = new float[4];
      matrix[2] = new float[4];
      matrix[3] = new float[4];

      matrix[0][0] = 1;
      matrix[0][1] = 3;
      matrix[0][2] = 1;
      matrix[0][3] = 2;
      matrix[1][0] = 4;
      matrix[1][1] = 2;
      matrix[1][2] = 2;
      matrix[1][3] = 3;
      matrix[2][0] = 2;
      matrix[2][1] = 2;
      matrix[2][2] = 1;
      matrix[2][3] = 3;
      matrix[3][0] = 2;
      matrix[3][1] = 4;
      matrix[3][2] = 1;
      matrix[3][3] = 2;

      t = new Transform(matrix);
   }

   @AfterMethod
   public void tearDown() throws Exception {
      t = null;
   }

   @Test
   public void TestLookAtNotNull() {
      Transform translation = Transform.LookAt(new Point3(1, 1, 1), new Point3(2, 2, 2), new Vector(0, 0, 1));

      Assert.assertNotNull(translation);

   }
}
