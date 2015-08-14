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

   public RelativeSpectralPowerDistribution(RelativeSpectralPowerDistribution other) {
      this();
      for (int i = 0; i < Buckets.length; i++) {
         Buckets[i] = other.Buckets[i];
      }
   }

   public SpectralPowerDistribution getSPD() {
      SpectralPowerDistribution spd = new SpectralPowerDistribution();

      for (int i = 0; i < Buckets.length; i++) {
         spd.Buckets[i] = Buckets[i];
      }

      return spd;
   }

   public void Merge(RelativeSpectralPowerDistribution other) {
      for (int i = 0; i < Buckets.length; i++) {
         Buckets[i] = (other.Buckets[i] + Buckets[i]) * .5f;
      }
   }
}
