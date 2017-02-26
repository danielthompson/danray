package net.danielthompson.danray.shading.fullspectrum;

/**
 * Created by daniel on 5/5/15.
 */
public class FullSpectralPowerDistribution extends FullSpectrum {
   public double Power;

   public FullSpectralPowerDistribution() {
      super();
   }

   public FullSpectralPowerDistribution(RelativeFullSpectralPowerDistribution rspd, double power) {
      this();
      for (int i = 0; i < rspd.Buckets.length; i++) {
         Buckets[i] = rspd.Buckets[i] * (float)power;
      }
      RecalculatePower();
   }

   public void add(FullSpectralPowerDistribution other) {
      for (int i = 0; i < Buckets.length; i++) {
         Buckets[i] += other.Buckets[i];
      }

      RecalculatePower();
   }

   private void RecalculatePower() {
      for (int i = 0; i < Buckets.length; i++) {
         Power += Buckets[i];
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

   public static FullSpectralPowerDistribution average(FullSpectralPowerDistribution[] spds) {
      FullSpectralPowerDistribution spd = new FullSpectralPowerDistribution();

      for (int i = 0; i < spd.Buckets.length; i++) {

         float sum = 0.0f;

         for (int j = 0; j < spds.length; j++) {
            sum += spds[j].Buckets[i];
         }

         spd.Buckets[i] = sum / spds.length;
      }

      spd.RecalculatePower();

      return spd;
   }

   public static FullSpectralPowerDistribution lerp(FullSpectralPowerDistribution spd1, float weight1, FullSpectralPowerDistribution spd2, float weight2) {
      FullSpectralPowerDistribution blend = new FullSpectralPowerDistribution();

      float scale = 1.0f / (weight1 + weight2);

      for (int i = 0; i < spd1.Buckets.length; i++) {
         blend.Buckets[i] = (float)(spd1.Buckets[i] * weight1 + spd2.Buckets[i] * weight2) * scale;
      }

      blend.RecalculatePower();

      return blend;
   }

   public static FullSpectralPowerDistribution add(FullSpectralPowerDistribution spd1, FullSpectralPowerDistribution spd2) {
      FullSpectralPowerDistribution spd = new FullSpectralPowerDistribution();

      spd.add(spd1);
      spd.add(spd2);

      spd.RecalculatePower();

      return spd;
   }

   public static FullSpectralPowerDistribution scale(FullSpectralPowerDistribution spd, double percentage) {
      FullSpectralPowerDistribution scaled = new FullSpectralPowerDistribution();

      for (int i = 0; i < spd.Buckets.length; i++) {
         scaled.Buckets[i] = spd.Buckets[i] * (float)percentage;
      }

      scaled.RecalculatePower();

      return scaled;
   }

   public void scale(double percentage) {
      for (int i = 0; i < Buckets.length; i++) {
         Buckets[i] *= percentage;
      }

      RecalculatePower();
   }

   public FullSpectralPowerDistribution reflectOff(FullSpectralReflectanceCurve curve) {
      FullSpectralPowerDistribution reflected = new FullSpectralPowerDistribution();

      if (curve == null) {
         int i = 2;
         ;
      }


      for (int i = 0; i < reflected.Buckets.length; i++) {
         reflected.Buckets[i] = Buckets[i] * curve.Buckets[i];
      }

      reflected.RecalculatePower();

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
