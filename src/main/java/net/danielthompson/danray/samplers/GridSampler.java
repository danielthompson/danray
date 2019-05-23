package net.danielthompson.danray.samplers;

import net.danielthompson.danray.structures.Point2;

/**
 * Created by daniel on 5/15/16.
 */
public class GridSampler extends AbstractSampler {

   private int rootNumSamples;

   private float differential;

   private float halfDifferential;

   public GridSampler(int samplesPerPixel) {
      super(samplesPerPixel);

      rootNumSamples = (int)Math.sqrt(samplesPerPixel);

      SamplesPerPixel = rootNumSamples * rootNumSamples;

      differential = 1.0f / (rootNumSamples);

      halfDifferential = differential * 0.5f;
   }

   @Override
   public Point2[] GetSamples(int x, int y, int n) {

      Point2[] pixels = new Point2[SamplesPerPixel];

      int index;

      float xOffset = x + halfDifferential;
      float yOffset = y + halfDifferential;

      for (int i = 0; i < rootNumSamples; i++) {
         index = i * rootNumSamples;

         for (int j = 0; j < rootNumSamples; j++) {
            pixels[index + j].x = xOffset + differential * j;
            pixels[index + j].y = yOffset + differential * i;;
         }
      }

      return pixels;
   }
}
