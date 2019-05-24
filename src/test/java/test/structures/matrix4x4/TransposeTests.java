package test.structures.matrix4x4;

import net.danielthompson.danray.structures.Matrix4x4;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by daniel on 2/15/15.
 */
public class TransposeTests {

   Matrix4x4 matrix;

   @BeforeMethod
   public void setUp() throws Exception {
      matrix = new Matrix4x4(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
   }

   @AfterMethod
   public void tearDown() throws Exception {
      matrix = null;
   }

   @Test
   public void TestTranspose() {
      Matrix4x4 transposed = matrix.transpose();

      Assert.assertEquals(transposed.matrix[0][0], matrix.matrix[0][0]);
      Assert.assertEquals(transposed.matrix[0][1], matrix.matrix[1][0]);
      Assert.assertEquals(transposed.matrix[0][2], matrix.matrix[2][0]);
      Assert.assertEquals(transposed.matrix[0][3], matrix.matrix[3][0]);
      Assert.assertEquals(transposed.matrix[1][0], matrix.matrix[0][1]);
      Assert.assertEquals(transposed.matrix[1][1], matrix.matrix[1][1]);
      Assert.assertEquals(transposed.matrix[1][2], matrix.matrix[2][1]);
      Assert.assertEquals(transposed.matrix[1][3], matrix.matrix[3][1]);
      Assert.assertEquals(transposed.matrix[2][0], matrix.matrix[0][2]);
      Assert.assertEquals(transposed.matrix[2][1], matrix.matrix[1][2]);
      Assert.assertEquals(transposed.matrix[2][2], matrix.matrix[2][2]);
      Assert.assertEquals(transposed.matrix[2][3], matrix.matrix[3][2]);
      Assert.assertEquals(transposed.matrix[3][0], matrix.matrix[0][3]);
      Assert.assertEquals(transposed.matrix[3][1], matrix.matrix[1][3]);
      Assert.assertEquals(transposed.matrix[3][2], matrix.matrix[2][3]);
      Assert.assertEquals(transposed.matrix[3][3], matrix.matrix[3][3]);
   }

   @Test
   public void TestTransposeTwice() {
      Matrix4x4 transposedOnce = matrix.transpose();
      Matrix4x4 transposedTwice = transposedOnce.transpose();

      Assert.assertEquals(matrix, transposedTwice);

   }
}
