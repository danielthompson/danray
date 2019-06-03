package test.samplers;

import net.danielthompson.danray.samplers.AbstractSampler;
import net.danielthompson.danray.samplers.RandomSampler;
import net.danielthompson.danray.samplers.StratifiedRandomSampler;
import net.danielthompson.danray.structures.Point2;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RandomSamplerTests {

   @Test
   public void test1() {
      AbstractSampler sampler = new RandomSampler(1);

      for (int i = 0; i < 100; i++) {
         Point2[] points = sampler.GetSamples(0, 0, 1);

         Assert.assertNotNull(points);
         Assert.assertEquals(points.length, 1);
         Assert.assertTrue(points[0].x >= 0.0f);
         Assert.assertTrue(points[0].x <= 1.0f);
         Assert.assertTrue(points[0].y >= 0.0f);
         Assert.assertTrue(points[0].y <= 1.0f);
      }
   }

   @Test
   public void test2() {
      AbstractSampler sampler = new RandomSampler(4);

      for (int i = 0; i < 100; i++) {
         Point2[] points = sampler.GetSamples(0, 0, 4);

         Assert.assertNotNull(points);
         Assert.assertEquals(points.length, 4);

         Assert.assertTrue(points[0].x >= 0.0f);
         Assert.assertTrue(points[0].x <= 1.0f);
         Assert.assertTrue(points[0].y >= 0.0f);
         Assert.assertTrue(points[0].y <= 1.0f);

         Assert.assertTrue(points[1].x >= 0.0f);
         Assert.assertTrue(points[1].x <= 1.0f);
         Assert.assertTrue(points[1].y >= 0.0f);
         Assert.assertTrue(points[1].y <= 1.0f);

         Assert.assertTrue(points[2].x >= 0.0f);
         Assert.assertTrue(points[2].x <= 1.0f);
         Assert.assertTrue(points[2].y >= 0.0f);
         Assert.assertTrue(points[2].y <= 1.0f);

         Assert.assertTrue(points[3].x >= 0.0f);
         Assert.assertTrue(points[3].x <= 1.0f);
         Assert.assertTrue(points[3].y >= 0.0f);
         Assert.assertTrue(points[3].y <= 1.0f);
      }
   }
}
