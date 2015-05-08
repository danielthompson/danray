package net.danielthompson.danray.shading;

/**
 * Created by daniel on 5/5/15.
 */
public class SpectralPowerDistribution {
   public float[] Buckets;

   public SpectralPowerDistribution() {
      Buckets = new float[48];
   }

   public double Power;

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
         tristimulusValue = func.Buckets[i] * Buckets[i];
      }

      return tristimulusValue;
   }
}
