package net.danielthompson.danray.shading;

/**
 * Created by daniel on 3/17/15.
 */
public class Spectrum {

   public float[] Buckets;

   public int numBuckets;

   public int startLambda = 380;
   public int endLambda = 760;
   public int bucketWidth = 10;

   public Spectrum() {
      Buckets = new float[39];
   }

   public static Spectrum Lerp(Spectrum s1, float w1, Spectrum s2, float w2) {
      Spectrum s = new Spectrum();
      for (int i = 0; i < s1.Buckets.length; i++) {
         s.Buckets[i] = s1.Buckets[i] * w1 + s2.Buckets[i] * w2;
      }

      return s;
   }

   public static SpectralPowerDistribution ConvertFromRGB(float r, float g, float b) {
      return ConvertFromRGB(r, RelativeSpectralPowerDistributionLibrary.Red.getSPD(),
            g, RelativeSpectralPowerDistributionLibrary.Green.getSPD(),
            b, RelativeSpectralPowerDistributionLibrary.Blue.getSPD());

   }

   public static SpectralPowerDistribution ConvertFromRGB(float r, SpectralPowerDistribution red, float g, SpectralPowerDistribution green, float b, SpectralPowerDistribution blue) {
      SpectralPowerDistribution s = new SpectralPowerDistribution();

      for (int i = 0; i < s.Buckets.length; i++) {
         s.Buckets[i] = red.Buckets[i] * r + green.Buckets[i] * g + blue.Buckets[i] * b;
      }

      return s;
   }

}
