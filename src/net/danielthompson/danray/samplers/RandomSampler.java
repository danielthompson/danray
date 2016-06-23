package net.danielthompson.danray.samplers;

import java.util.Random;

/**
 * Created by daniel on 5/15/16.
 */
public class RandomSampler extends AbstractSampler {

   @Override
   public float[][] GetSamples(int x, int y, int n) {
      float[][] pixels = new float[n][2];

      for (int i = 0; i < n; i++) {
         pixels[i][0] = x + AbstractSampler.Random.nextFloat();
         pixels[i][1] = y + AbstractSampler.Random.nextFloat();
      }

      return pixels;
   }
}
