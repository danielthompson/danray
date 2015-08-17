package test.shading.bxdf;

import net.danielthompson.danray.shading.*;
import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.shading.bxdf.MirrorBRDF;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.structures.*;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Created by dthompson on 08 May 15.
 */
public class MirrorBRDFTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }
//
//   @Test
//   public void testGetVectorInPDF() throws Exception {
//      BRDF brdf = new MirrorBRDF();
//
//      Normal n = new Normal(0, 1, 0);
//      Vector v = new Vector(1, -1, 0);
//      v.Normalize();
//
//      Vector expected = new Vector(1, 1, 0);
//      expected.Normalize();
//
//      Vector actual = brdf.getVectorInPDF(n, v);
//
//      Assert.assertEquals(actual.X, expected.X);
//      Assert.assertEquals(actual.Y, expected.Y);
//      Assert.assertEquals(actual.Z, expected.Z);
//   }


   @DataProvider(name = "MirrorBRDFProvider")
   public Object[][] MirrorBRDFProvider() {

      ArrayList<Object[]> list = new ArrayList<Object[]>();

      Normal n;
      Vector v;
      Vector expected;

      // case 1

      n = new Normal(0, 1, 0);
      v = new Vector(1, -1, 0);
      v.Normalize();

      expected = new Vector(1, 1, 0);
      expected.Normalize();

      list.add(new Object[] {n, v, expected});

      // case 2

      n = new Normal(0, 1, 0);
      v = new Vector(2, -1, 0);
      v.Normalize();

      expected = new Vector(2, 1, 0);
      expected.Normalize();

      list.add(new Object[] {n, v, expected});

      // case 3

      n = new Normal(0, 1, 0);
      v = new Vector(0, -1, 0);
      v.Normalize();

      expected = new Vector(0, 1, 0);
      expected.Normalize();

      list.add(new Object[] {n, v, expected});

      int count = list.size();

      Object[][] cases = new Object[count][3];

      for (int i = 0; i < count; i++) {
         cases[i] = list.get(i);
      }

      return cases;
   }

   @Test(dataProvider = "MirrorBRDFProvider")
   public void testGetVectorInPDF(Normal n, Vector v, Vector expected) {
      BRDF brdf = new MirrorBRDF();

      Vector actual = brdf.getVectorInPDF(n, v);

      Assert.assertNotNull(actual);

      Assert.assertEquals(actual.X, expected.X, Constants.NumericalDelta);
      Assert.assertEquals(actual.Y, expected.Y, Constants.NumericalDelta);
      Assert.assertEquals(actual.Z, expected.Z, Constants.NumericalDelta);

   }


}
