package net.danielthompson.danray.shading;

/**
 * Created by daniel on 5/5/15.
 */
public class RelativeSpectralPowerDistribution extends Spectrum {

   public RelativeSpectralPowerDistribution() {
      super();
   }

   public RelativeSpectralPowerDistribution(SpectralPowerDistribution spd) {
      this();
      for (int i = 0; i < Buckets.length; i++) {
         Buckets[i] = spd.Buckets[i] / (float)spd.Power;
      }
   }
}
