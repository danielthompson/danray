package net.danielthompson.danray.structures;

import net.danielthompson.danray.shading.SpectralPowerDistribution;

import java.awt.Color;

/**
 * Created by daniel on 1/1/15.
 */
public class Sample {
   public Color Color;
   public Statistics Statistics;
   public SpectralPowerDistribution SpectralPowerDistribution;
   public int KDHeatCount;

   public Sample() {
      Statistics = new Statistics();
   }
}
