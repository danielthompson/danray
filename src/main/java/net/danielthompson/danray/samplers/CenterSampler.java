package net.danielthompson.danray.samplers;

import net.danielthompson.danray.structures.Point2;

/**
 * Created by daniel on 5/15/16.
 */
public class CenterSampler extends AbstractSampler {

   public CenterSampler(int samplesPerPixel) {
      super(samplesPerPixel);
   }

   @Override
   public Point2[] GetSamples(int x, int y, int n) {
      Point2[] pixels = new Point2[n];

      float xf = x + 0.5f;
      float yf = y + 0.5f;

      for (int i = 0; i < n; i++) {
         pixels[i] = new Point2(xf, yf);
      }

      return pixels;
   }
}
