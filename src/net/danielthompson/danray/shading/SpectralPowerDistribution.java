package net.danielthompson.danray.shading;

/**
 * Created by daniel on 5/5/15.
 */
public class SpectralPowerDistribution extends Spectrum {
   public double Power;

   public SpectralPowerDistribution() {
      super();
   }

   public SpectralPowerDistribution(RelativeSpectralPowerDistribution rspd, double power) {
      this();
      for (int i = 0; i < rspd.Buckets.length; i++) {
         Buckets[i] = rspd.Buckets[i] * (float)power;
      }
      Power = power;
   }

   public void add(SpectralPowerDistribution other) {
      for (int i = 0; i < Buckets.length; i++) {
         Buckets[i] += other.Buckets[i];
      }
   }

   public static SpectralPowerDistribution scale(SpectralPowerDistribution spd, double percentage) {
      SpectralPowerDistribution scaled = new SpectralPowerDistribution();

      for (int i = 0; i < spd.Buckets.length; i++) {
         scaled.Buckets[i] = spd.Buckets[i] * (float)percentage;
      }

      return scaled;
   }

   public void scale(double percentage) {
      for (int i = 0; i < Buckets.length; i++) {
         Buckets[i] *= percentage;
      }
   }

   public SpectralPowerDistribution reflectOff(SpectralReflectanceCurve curve) {
      SpectralPowerDistribution reflected = new SpectralPowerDistribution();

      for (int i = 0; i < reflected.Buckets.length; i++) {
         reflected.Buckets[i] = Buckets[i] * curve.Buckets[i];
      }

      return reflected;
   }

   public float apply(StandardObserverColorMatchingFunction func) {
      float tristimulusValue = 0.0f;

      for (int i = 0; i < Buckets.length; i++) {
         tristimulusValue = tristimulusValue + (func.Buckets[i] * Buckets[i]);
      }

      return tristimulusValue;
   }
}
