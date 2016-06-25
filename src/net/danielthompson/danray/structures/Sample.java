package net.danielthompson.danray.structures;

import net.danielthompson.danray.shading.fullspectrum.FullSpectralPowerDistribution;

import java.awt.Color;

/**
 * Created by daniel on 1/1/15.
 */
public class Sample {
   public Color Color;
   public Statistics Statistics;
   public FullSpectralPowerDistribution FullSpectralPowerDistribution;
   public int KDHeatCount;

   public Sample() {
      Statistics = new Statistics();
   }
}
