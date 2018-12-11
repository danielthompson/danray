package net.danielthompson.danray.samplers;

import net.danielthompson.danray.utility.GeometryCalculations;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by daniel on 5/15/16.
 */
public class RandomSampler extends AbstractSampler {

   public RandomSampler(int samplesPerPixel) {
      super(samplesPerPixel);
   }

   @Override
   public float[][] GetSamples(int x, int y, int n) {
      float[][] pixels = new float[n][2];

      for (int i = 0; i < n; i++) {
         pixels[i][0] = (float) (x + GeometryCalculations.splitRandom.get().nextDouble());
         pixels[i][1] = (float) (y + GeometryCalculations.splitRandom.get().nextDouble());
      }

      return pixels;
   }
}
