package net.danielthompson.danray.shading.fullspectrum;

/**
 * Created by daniel on 5/5/15.
 */
public class RelativeFullSpectralPowerDistribution extends FullSpectrum {

   public RelativeFullSpectralPowerDistribution() {
      super();
   }

   public RelativeFullSpectralPowerDistribution(FullSpectralPowerDistribution spd) {
      this();
      for (int i = 0; i < Buckets.length; i++) {
         Buckets[i] = spd.Buckets[i] / (float)spd.Power;
      }
   }

   public RelativeFullSpectralPowerDistribution(RelativeFullSpectralPowerDistribution other) {
      this();
      System.arraycopy(other.Buckets, 0, Buckets, 0, Buckets.length);
   }

   public FullSpectralPowerDistribution getSPD() {
      FullSpectralPowerDistribution spd = new FullSpectralPowerDistribution();

      System.arraycopy(Buckets, 0, spd.Buckets, 0, Buckets.length);

      return spd;
   }

   public void Merge(RelativeFullSpectralPowerDistribution other) {
      for (int i = 0; i < Buckets.length; i++) {
         Buckets[i] = (other.Buckets[i] + Buckets[i]) * .5f;
      }
   }
}
