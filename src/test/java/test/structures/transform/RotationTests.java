package test.structures.transform;

import junit.framework.Assert;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by daniel on 2/15/15.
 */
public class RotationTests {
   Transform t;

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

      t = new Transform(matrix);
   }

   @AfterMethod
   public void tearDown() throws Exception {
      t = null;
   }

   @Test
   public void TestRotateXNotNull() {
      Transform translation = Transform.RotateX(-5);

      Assert.assertNotNull(translation);
   }

   @Test
   public void TestRotateYNotNull() {
      Transform translation = Transform.RotateY(-5);

      Assert.assertNotNull(translation);
   }

   @Test
   public void TestRotateZNotNull() {
      Transform translation = Transform.RotateZ(-5);

      Assert.assertNotNull(translation);
   }

   @Test
   public void TestRotateNotNull() {
      Transform translation = Transform.Rotate(5, new Vector(-1, -1, -1));

      Assert.assertNotNull(translation);
   }
}
