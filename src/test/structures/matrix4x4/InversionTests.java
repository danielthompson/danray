package test.structures.matrix4x4;

import junit.framework.Assert;
import net.danielthompson.danray.structures.Matrix4x4;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by daniel on 2/15/15.
 */
public class InversionTests {
   Matrix4x4 m;

   @BeforeMethod
   public void setUp() throws Exception {
      double[][] matrix = new double[4][4];

      matrix[0] = new double[4];
      matrix[1] = new double[4];
      matrix[2] = new double[4];
      matrix[3] = new double[4];

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

      m = new Matrix4x4(matrix);
   }

   @AfterMethod
   public void tearDown() throws Exception {
      m = null;
   }

   @Test
   public void TestInversionNotNull() {
      Matrix4x4 inversion = m.Inverse();

      Assert.assertNotNull(inversion);
   }

}
