package net.danielthompson.danray.shading;

import java.awt.Color;

/**
 * Created by daniel on 6/25/16.
 */
public class SpectralPowerDistribution extends Spectrum {

   public SpectralPowerDistribution() {

   }

   public SpectralPowerDistribution(float r, float g, float b)  {
      super (r, g, b);
   }

   public SpectralPowerDistribution(Color c) {
      super (c);
   }

   public SpectralPowerDistribution(Color c, float power) {
      super (c, power);
   }

   public static SpectralPowerDistribution Lerp(SpectralPowerDistribution s1, float w1, SpectralPowerDistribution s2, float w2) {
      SpectralPowerDistribution s = new SpectralPowerDistribution();

      s.R = s1.R * w1 + s2.R * w2;
      s.G = s1.G * w1 + s2.G * w2;
      s.B = s1.B * w1 + s2.B * w2;

      return s;
   }



   public static SpectralPowerDistribution add(SpectralPowerDistribution s1, SpectralPowerDistribution s2) {
      SpectralPowerDistribution s = new SpectralPowerDistribution();

      s.R = s1.R + s2.R;
      s.G = s1.G + s2.G;
      s.B = s1.B + s2.B;

      return s;
   }

   public static SpectralPowerDistribution scale(SpectralPowerDistribution spd, float percentage) {
      SpectralPowerDistribution scaled = new SpectralPowerDistribution();

      scaled.R = spd.R * percentage;
      scaled.G = spd.G * percentage;
      scaled.B = spd.B * percentage;

      return scaled;
   }

   public void scale(float percentage) {
      R *= percentage;
      G *= percentage;
      B *= percentage;
   }

   public SpectralPowerDistribution reflectOff(ReflectanceSpectrum curve) {
      SpectralPowerDistribution reflected = new SpectralPowerDistribution();

      reflected.R = R * curve.R;
      reflected.G = G * curve.G;
      reflected.B = B * curve.B;

      return reflected;
   }


}
