package net.danielthompson.danray.shading.fullspectrum;

/**
 * Created by daniel on 3/17/15.
 */
public class FullSpectrum {

   public float[] Buckets;

   public int numBuckets;

   public int startLambda = 380;
   public int endLambda = 760;
   public int bucketWidth = 10;

   public FullSpectrum() {
      Buckets = new float[39];
   }

   public static FullSpectrum Lerp(FullSpectrum s1, float w1, FullSpectrum s2, float w2) {
      FullSpectrum s = new FullSpectrum();
      for (int i = 0; i < s1.Buckets.length; i++) {
         s.Buckets[i] = s1.Buckets[i] * w1 + s2.Buckets[i] * w2;
      }

      return s;
   }

   public static FullSpectralPowerDistribution ConvertFromRGB(float r, float g, float b) {
      return ConvertFromRGB(r, RelativeFullSpectralPowerDistributionLibrary.Red.getSPD(),
            g, RelativeFullSpectralPowerDistributionLibrary.Green.getSPD(),
            b, RelativeFullSpectralPowerDistributionLibrary.Blue.getSPD());

   }

   public static FullSpectralPowerDistribution ConvertFromRGB(float r, FullSpectralPowerDistribution red, float g, FullSpectralPowerDistribution green, float b, FullSpectralPowerDistribution blue) {
      FullSpectralPowerDistribution s = new FullSpectralPowerDistribution();

      for (int i = 0; i < s.Buckets.length; i++) {
         s.Buckets[i] = red.Buckets[i] * r + green.Buckets[i] * g + blue.Buckets[i] * b;
      }

      return s;
   }

}
