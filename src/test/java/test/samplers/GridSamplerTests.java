package test.samplers;

import net.danielthompson.danray.samplers.AbstractSampler;
import net.danielthompson.danray.samplers.GridSampler;
import net.danielthompson.danray.structures.Point2;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GridSamplerTests {

   @Test
   public void test1() {
      AbstractSampler sampler = new GridSampler(1);

      for (int i = 0; i < 100; i++) {
         Point2[] points = sampler.GetSamples(0, 0, 1);

         Assert.assertNotNull(points);
         Assert.assertEquals(points.length, 1);
         Assert.assertEquals(points[0].x, 0.5f);
         Assert.assertEquals(points[0].y, 0.5f);
      }
   }

   @Test
   public void test2() {
      AbstractSampler sampler = new GridSampler(4);

      for (int i = 0; i < 100; i++) {
         Point2[] points = sampler.GetSamples(0, 0, 4);

         Assert.assertNotNull(points);
         Assert.assertEquals(points.length, 4);

         Assert.assertEquals(points[0].x, 0.25f);
         Assert.assertEquals(points[0].y, 0.25f);

         Assert.assertEquals(points[1].x, 0.75f);
         Assert.assertEquals(points[1].y, 0.25f);

         Assert.assertEquals(points[2].x, 0.25f);
         Assert.assertEquals(points[2].y, 0.75f);

         Assert.assertEquals(points[3].x, 0.75f);
         Assert.assertEquals(points[3].y, 0.75f);
      }
   }
}
