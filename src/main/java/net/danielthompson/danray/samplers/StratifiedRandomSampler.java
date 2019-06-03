package net.danielthompson.danray.samplers;

import net.danielthompson.danray.structures.Point2;
import net.danielthompson.danray.utility.GeometryCalculations;

import java.util.Random;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by daniel on 5/15/16.
 */
public class StratifiedRandomSampler extends AbstractSampler {

   private final int rootNumSamples;
   private final float differential;

   public StratifiedRandomSampler(final int samplesPerPixel) {
      super(samplesPerPixel);
      rootNumSamples = (int)Math.sqrt(samplesPerPixel);
      SamplesPerPixel = rootNumSamples * rootNumSamples;
      differential = 1.0f / (rootNumSamples);
   }

   @Override
   public Point2[] GetSamples(final int x, final int y, final int n) {
      final Point2[] pixels = new Point2[SamplesPerPixel];

      final Random random = ThreadLocalRandom.current();

      int index;

      final float xFloat = (float)x;
      final float yFloat = (float)y;

      for (int i = 0; i < rootNumSamples; i++) {
         index = i * rootNumSamples;

         for (int j = 0; j < rootNumSamples; j++) {
            final float xValue = differential * random.nextFloat() + differential * i + xFloat;
            final float yValue = differential * random.nextFloat() + differential * j + yFloat;
            pixels[index + j] = new Point2(xValue, yValue);
         }
      }
      return pixels;
   }
}
