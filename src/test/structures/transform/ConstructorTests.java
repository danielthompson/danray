package test.structures.transform;

import net.danielthompson.danray.structures.Transform;
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
      Transform t = new Transform();
   }

   @Test
   public void TestValuesConstructor() {
      double[][] matrix = new double[4][4];

      matrix[0] = new double[4];
      matrix[1] = new double[4];
      matrix[2] = new double[4];
      matrix[3] = new double[4];

      matrix[0][0] = 1;
      matrix[1][1] = 1;
      matrix[2][2] = 1;
      matrix[3][3] = 1;

      Transform t = new Transform(matrix);

   }
}
