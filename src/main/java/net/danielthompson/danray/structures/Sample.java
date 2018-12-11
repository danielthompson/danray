package net.danielthompson.danray.structures;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shading.fullspectrum.FullSpectralPowerDistribution;

import java.awt.Color;

/**
 * Created by daniel on 1/1/15.
 */
public class Sample {

   public SpectralPowerDistribution SpectralPowerDistribution;

   public float x, y;

   public int KDHeatCount;

   @Override
   public String toString() {

      float sum = 0;

      if (SpectralPowerDistribution != null) {
         sum += SpectralPowerDistribution.R;
         sum += SpectralPowerDistribution.G;
         sum += SpectralPowerDistribution.B;
      }

      return "Total: " + sum;
   }
}
