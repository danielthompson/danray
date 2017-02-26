package test.structures.matrix4x4;

import net.danielthompson.danray.structures.Matrix4x4;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by daniel on 2/15/15.
 */

public class ConstructorTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void TestDefaultConstructor() {
      Matrix4x4 m = new Matrix4x4();
   }

   @Test
   public void TestArrayConstructor() {
      double[][] matrix = new double[4][4];

      matrix[0] = new double[4];
      matrix[1] = new double[4];
      matrix[2] = new double[4];
      matrix[3] = new double[4];

      matrix[0][0] = 1;
      matrix[1][1] = 1;
      matrix[2][2] = 1;
      matrix[3][3] = 1;

      Matrix4x4 m = new Matrix4x4(matrix);
   }

   @Test
   public void TestValuesConstructor() {

      Matrix4x4 m = new Matrix4x4(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
   }
}
