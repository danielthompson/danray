package net.danielthompson.danray.samplers;

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
   public float[][] GetSamples(int x, int y, int n) {

      float[][] pixels = new float[SamplesPerPixel][2];

      int index;

      float xOffset = x + halfDifferential;
      float yOffset = y + halfDifferential;

      for (int i = 0; i < rootNumSamples; i++) {
         index = i * rootNumSamples;

         for (int j = 0; j < rootNumSamples; j++) {
            pixels[index + j][0] = xOffset + differential * j;
            pixels[index + j][1] = yOffset + differential * i;;
         }
      }

      return pixels;
   }
}
