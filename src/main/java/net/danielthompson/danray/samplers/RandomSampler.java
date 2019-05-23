package net.danielthompson.danray.samplers;

import net.danielthompson.danray.structures.Point2;
import net.danielthompson.danray.utility.GeometryCalculations;

import java.util.SplittableRandom;

/**
 * Created by daniel on 5/15/16.
 */
public class RandomSampler extends AbstractSampler {

   public RandomSampler(int samplesPerPixel) {
      super(samplesPerPixel);
   }

   @Override
   public Point2[] GetSamples(int x, int y, int n) {
      Point2[] pixels = new Point2[n];

      SplittableRandom random = GeometryCalculations.splitRandom.get();

      for (int i = 0; i < n; i++) {
         pixels[i] = new Point2((float) (x + random.nextDouble()), (float) (y + random.nextDouble()));
      }

      return pixels;
   }
}
