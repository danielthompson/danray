package net.danielthompson.danray.samplers;

import net.danielthompson.danray.structures.Point2;
import net.danielthompson.danray.utility.GeometryCalculations;

import java.util.Random;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by daniel on 5/15/16.
 */
public class RandomSampler extends AbstractSampler {

   public RandomSampler(final int samplesPerPixel) {
      super(samplesPerPixel);
   }

   @Override
   public Point2[] GetSamples(final int x, final int y, final int n) {
      final Point2[] pixels = new Point2[n];

      final Random random = ThreadLocalRandom.current();

      final float xFloat = (float)x;
      final float yFloat = (float)y;

      for (int i = 0; i < n; i++) {
         pixels[i] = new Point2(xFloat + random.nextFloat(), yFloat + random.nextFloat());
      }

      return pixels;
   }
}
