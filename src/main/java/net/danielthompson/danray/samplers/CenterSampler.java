package net.danielthompson.danray.samplers;

/**
 * Created by daniel on 5/15/16.
 */
public class CenterSampler extends AbstractSampler {

   @Override
   public float[][] GetSamples(int x, int y, int n) {
      float[][] pixels = new float[n][2];

      for (int i = 0; i < n; i++) {
         pixels[i][0] = x + 0.5f;
         pixels[i][1] = y + 0.5f;
      }

      return pixels;
   }
}
