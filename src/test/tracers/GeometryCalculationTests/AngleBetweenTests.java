package test.tracers.GeometryCalculationTests;

import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Vector;
import net.danielthompson.danray.utility.GeometryCalculations;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * Created by daniel on 8/16/15.
 */
public class AngleBetweenTests {
//
//   @Test
//   public void testAngleBetween() {
//      Vector v1 = new Vector(0, 1, 0);
//      Vector v2 = new Vector(1, 0, 0);
//
//      double expectedAngle = 90;
//
//      double actualAngle = GeometryCalculations.angleBetween(v1, v2);
//
//      Assert.assertEquals(actualAngle, expectedAngle);
//   }


   @DataProvider(name = "AngleBetweenProvider")
   public Object[][] AngleBetweenProvider() {

      ArrayList<Object[]> list = new ArrayList<Object[]>();

      double root3 = Math.sqrt(3);
      double root2 = Math.sqrt(2);

      Vector v1, v2;
      double expected;

      // case 1
      v1 = new Vector(0, 1, 0);
      v2 = new Vector(1, 0, 0);
      expected = 90;
      list.add(new Object[] {v1, v2, expected});

      // case 2
      v1 = new Vector(1, 1, 0);
      v2 = new Vector(1, 1, 0);
      expected = 0;
      list.add(new Object[] {v1, v2, expected});

      // case 3
      v1 = new Vector(1, 0, 0);
      v2 = new Vector(1, 1, 0);
      expected = 45;
      list.add(new Object[] {v1, v2, expected});

      // case 4
      v1 = new Vector(root3, 1, 0);
      v2 = new Vector(0, 1, 0);
      expected = 60;
      list.add(new Object[] {v1, v2, expected});

      // case 5
      v1 = new Vector(root3, 1, 0);
      v2 = new Vector(1, 0, 0);
      expected = 30;
      list.add(new Object[] {v1, v2, expected});

      // case 6
      v1 = new Vector(root3, 1, 0);
      v2 = new Vector(1, 1, 0);
      expected = 15;
      list.add(new Object[] {v1, v2, expected});

      // case 7
      v1 = new Vector(-1, 1, 0);
      v2 = new Vector(1, 1, 0);
      expected = 90;
      list.add(new Object[] {v1, v2, expected});

      // case 8
      v1 = new Vector(-root3, 1, 0);
      v2 = new Vector(root3, 1, 0);
      expected = 120;
      list.add(new Object[] {v1, v2, expected});

      // case 8
      v1 = new Vector(-1, 0, 0);
      v2 = new Vector(root3, 1, 0);
      expected = 150;
      list.add(new Object[] {v1, v2, expected});

      int count = list.size();

      Object[][] cases = new Object[count][3];

      for (int i = 0; i < count; i++) {
         cases[i] = list.get(i);
      }

      return cases;
   }

   @Test(dataProvider = "AngleBetweenProvider")
   public void testGetAngleBetween(Vector v1, Vector v2, double expected) {

      double actual = GeometryCalculations.angleBetween(v1, v2);

      Assert.assertEquals(actual, expected, Constants.Epsilon *1000000);

   }

}
