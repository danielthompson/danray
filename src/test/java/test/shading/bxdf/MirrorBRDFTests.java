package test.shading.bxdf;

import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.shading.bxdf.reflect.SpecularBRDF;
import net.danielthompson.danray.structures.*;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.AssertHelper;

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
//      reflect brdf = new SpecularBRDF();
//
//      Normal n = new Normal(0, 1, 0);
//      Vector v = new Vector(1, -1, 0);
//      v.normalize();
//
//      Vector expected = new Vector(1, 1, 0);
//      expected.normalize();
//
//      Vector actual = brdf.getVectorInPDF(n, v);
//
//      AssertHelper.assertEquals(actual.x, expected.x);
//      AssertHelper.assertEquals(actual.y, expected.y);
//      AssertHelper.assertEquals(actual.z, expected.z);
//   }


   @DataProvider(name = "MirrorBRDFProvider")
   public Object[][] MirrorBRDFProvider() {

      ArrayList<Object[]> list = new ArrayList<Object[]>();

      Normal n;
      Vector3 v;
      Vector3 expected;

      // case 1

      n = new Normal(0, 1, 0);
      v = new Vector3(1, -1, 0);
      v.normalize();

      expected = new Vector3(1, 1, 0);
      expected.normalize();

      list.add(new Object[] {n, v, expected});

      // case 2

      n = new Normal(0, 1, 0);
      v = new Vector3(2, -1, 0);
      v.normalize();

      expected = new Vector3(2, 1, 0);
      expected.normalize();

      list.add(new Object[] {n, v, expected});

      // case 3

      n = new Normal(0, 1, 0);
      v = new Vector3(0, -1, 0);
      v.normalize();

      expected = new Vector3(0, 1, 0);
      expected.normalize();

      list.add(new Object[] {n, v, expected});

      int count = list.size();

      Object[][] cases = new Object[count][3];

      for (int i = 0; i < count; i++) {
         cases[i] = list.get(i);
      }

      return cases;
   }

   @Test(dataProvider = "MirrorBRDFProvider")
   public void testGetVectorInPDF(Normal n, Vector3 v, Vector3 expected) {
      BRDF brdf = new SpecularBRDF();

      Vector3 actual = brdf.getVectorInPDF(n, v, 1, 1);

      Assert.assertNotNull(actual);

      AssertHelper.assertEquals(actual, expected);
   }
}