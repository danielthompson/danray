package net.danielthompson.danray.shading;

/**
 * Created by daniel on 5/5/15.
 */
public class RelativeSpectralPowerDistribution {
   public float[] Buckets;

   public RelativeSpectralPowerDistribution() {
      Buckets = new float[50];
   }

   public RelativeSpectralPowerDistribution(SpectralPowerDistribution spd) {
      this();
      for (int i = 0; i < Buckets.length; i++) {
         Buckets[i] = spd.Buckets[i] / (float)spd.Power;
      }
   }
}
