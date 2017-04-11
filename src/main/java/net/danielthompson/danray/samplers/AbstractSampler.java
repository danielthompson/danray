package net.danielthompson.danray.samplers;

import java.util.Random;

/**
 * Created by daniel on 5/15/16.
 */
public abstract class AbstractSampler {
   public static Random Random = new Random();

   public int SamplesPerPixel;

   public AbstractSampler(int samplesPerPixel) {
      SamplesPerPixel = samplesPerPixel;
   }

   public abstract float[][] GetSamples(int x, int y, int n);
}
