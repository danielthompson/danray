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

   public float max() {
      float max = -Float.MAX_VALUE;

      for (int i = 0; i < Buckets.length; i++) {
         if (Buckets[i] > max)
            max = Buckets[i];
      }

      return max;
   }

   public float min() {
      float min = Float.MAX_VALUE;

      for (int i = 0; i < Buckets.length; i++) {
         if (Buckets[i] < min)
            min = Buckets[i];
      }

      return min;
   }

   public static SpectralPowerDistribution average(SpectralPowerDistribution[] spds) {
      SpectralPowerDistribution spd = new SpectralPowerDistribution();

      for (int i = 0; i < spd.Buckets.length; i++) {

         float sum = 0.0f;

         for (int j = 0; j < spds.length; j++) {
            sum += spds[j].Buckets[i];
         }

         spd.Buckets[i] = sum / spds.length;
      }

      return spd;
   }

   public static SpectralPowerDistribution lerp(SpectralPowerDistribution spd1, float weight1, SpectralPowerDistribution spd2, float weight2) {
      SpectralPowerDistribution blend = new SpectralPowerDistribution();

      float scale = 1.0f / (weight1 + weight2);

      for (int i = 0; i < spd1.Buckets.length; i++) {
         blend.Buckets[i] = (float)(spd1.Buckets[i] * weight1 + spd2.Buckets[i] * weight2) * scale;
      }

      return blend;
   }

   public static SpectralPowerDistribution add(SpectralPowerDistribution spd1, SpectralPowerDistribution spd2) {
      SpectralPowerDistribution spd = new SpectralPowerDistribution();

      spd.add(spd1);
      spd.add(spd2);

      return spd;
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
         reflected.Buckets[i] = Buckets[i] * curve.Buckets[i] * 100f;
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
