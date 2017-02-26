package test.structures.matrix4x4;

import junit.framework.Assert;
import net.danielthompson.danray.structures.Matrix4x4;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by daniel on 2/16/15.
 */
public class MultiplyTests {

   Matrix4x4 matrix;

   @BeforeMethod
   public void setUp() throws Exception {
      matrix = new Matrix4x4(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
   }

   @AfterMethod
   public void tearDown() throws Exception {
      matrix = null;
   }

   @DataProvider(name = "MultiplyDataProvider")
   public Object[][] MultiplyDataProvider() {
      return new Object[][] {
            {
                  new Matrix4x4(1, 0, 0, 0,  0, 1, 0, 0,  0, 0, 1, 0,  0, 0, 0, 1),
                  new Matrix4x4(1, 0, 0, 0,  0, 1, 0, 0,  0, 0, 1, 0,  0, 0, 0, 1),
                  new Matrix4x4(1, 0, 0, 0,  0, 1, 0, 0,  0, 0, 1, 0,  0, 0, 0, 1),
            },

            {
                  new Matrix4x4( 1,   2,   3,   4,    5,   6,   7,   8,    9,  10,  11,  12,   13,  14,  15,  16),
                  new Matrix4x4( 1,   2,   3,   4,    5,   6,   7,   8,    9,  10,  11,  12,   13,  14,  15,  16),
                  new Matrix4x4(90, 100, 110, 120,  202, 228, 254, 280,  314, 356, 398, 440,  426, 484, 542, 600)
            },
            {
                  new Matrix4x4( 5,  3,  1,  4,   3,  4,  2,  3,   1,  2,  4,  7,    9,   5,   8,   3),
                  new Matrix4x4( 4,  6,  7,  1,   2,  3,  4,  7,   9,  6,  5,  8,    7,   4,   1,   3),
                  new Matrix4x4(63, 61, 56, 46,  59, 54, 50, 56,  93, 64, 42, 68,  139, 129, 126, 117),
            }
      };
   }

   @Test(dataProvider = "MultiplyDataProvider")
   public void TestMultiply(Matrix4x4 m1, Matrix4x4 m2, Matrix4x4 expected) {

      Matrix4x4 actual = m1.Multiply(m2);

      Assert.assertNotNull(actual);
      Assert.assertEquals(expected, actual);
   }
}
