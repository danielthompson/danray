package test.tracers.GeometryCalculationTests;

import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector3;
import net.danielthompson.danray.utility.GeometryCalculations;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * Created by daniel on 8/16/15.
 */
public class RadiansBetweenTests {
//
//   @Test
//   public void testAngleBetween() {
//      Vector v1 = new Vector(0, 1, 0);
//      Vector n1 = new normal(1, 0, 0);
//
//      float expectedAngle = 90;
//
//      float actualAngle = GeometryCalculations.radiansBetween(v1, n1);
//
//      Assert.assertEquals(actualAngle, expectedAngle);
//   }


   @DataProvider(name = "RadiansBetweenProvider")
   public Object[][] RadiansBetweenProvider() {

      ArrayList<Object[]> list = new ArrayList<Object[]>();

      Vector3 v1;
      Normal n1;
      float expected;

      // case 1
      v1 = new Vector3(0, 1, 0);
      n1 = new Normal(1, 0, 0);
      expected = Constants.PIOver2;
      list.add(new Object[] {v1, n1, expected});

      // case 2
      v1 = new Vector3(1, 1, 0);
      n1 = new Normal(1, 1, 0);
      expected = 0;
      list.add(new Object[] {v1, n1, expected});

      // case 3
      v1 = new Vector3(1, 0, 0);
      n1 = new Normal(1, 1, 0);
      expected = Constants.PIOver4;
      list.add(new Object[] {v1, n1, expected});

      // case 4
      v1 = new Vector3(Constants.Root3, 1, 0);
      n1 = new Normal(0, 1, 0);
      expected = Constants.PIOver3;
      list.add(new Object[] {v1, n1, expected});

      // case 5
      v1 = new Vector3(Constants.Root3, 1, 0);
      n1 = new Normal(1, 0, 0);
      expected = Constants.PIOver6;
      list.add(new Object[] {v1, n1, expected});

      // case 6
      v1 = new Vector3(Constants.Root3, 1, 0);
      n1 = new Normal(1, 1, 0);
      expected = Constants.PIOver12;
      list.add(new Object[] {v1, n1, expected});

      // case 7
      v1 = new Vector3(-1, 1, 0);
      n1 = new Normal(1, 1, 0);
      expected = Constants.PIOver2;
      list.add(new Object[] {v1, n1, expected});

      // case 8
      v1 = new Vector3(-Constants.Root3, 1, 0);
      n1 = new Normal(Constants.Root3, 1, 0);
      expected = 2.0f * Constants.PIOver3;
      list.add(new Object[] {v1, n1, expected});

      // case 8
      v1 = new Vector3(-1, 0, 0);
      n1 = new Normal(Constants.Root3, 1, 0);
      expected = 5.0f * Constants.PIOver6;
      list.add(new Object[] {v1, n1, expected});

      int count = list.size();

      Object[][] cases = new Object[count][3];

      for (int i = 0; i < count; i++) {
         cases[i] = list.get(i);
      }

      return cases;
   }

   @Test(dataProvider = "RadiansBetweenProvider")
   public void testGetRadiansBetween(Vector3 v1, Normal n1, float expected) {

      float actual = GeometryCalculations.radiansBetween(v1, n1);

      Assert.assertEquals(actual, expected, Constants.UnitTestDelta);

   }

}
