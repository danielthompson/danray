package net.danielthompson.danray.samplers;

import net.danielthompson.danray.structures.Point2;

/**
 * Created by daniel on 5/15/16.
 */
public class GridSampler extends AbstractSampler {

   private final int rootNumSamples;
   private final float differential;
   private final float halfDifferential;

   public GridSampler(final int samplesPerPixel) {
      super(samplesPerPixel);
      rootNumSamples = (int)Math.sqrt(samplesPerPixel);
      SamplesPerPixel = rootNumSamples * rootNumSamples;
      differential = 1.0f / (rootNumSamples);
      halfDifferential = differential * 0.5f;
   }

   @Override
   public Point2[] GetSamples(final int x, final int y, final int n) {
      final Point2[] pixels = new Point2[SamplesPerPixel];
      int index;
      final float xOffset = x + halfDifferential;
      final float yOffset = y + halfDifferential;

      for (int i = 0; i < rootNumSamples; i++) {
         index = i * rootNumSamples;
         final float yValue = yOffset + differential * i;

         for (int j = 0; j < rootNumSamples; j++) {
            pixels[index + j] = new Point2(xOffset + differential * j, yValue);
         }
      }

      return pixels;
   }
}
