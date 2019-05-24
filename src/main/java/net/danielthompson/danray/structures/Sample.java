package net.danielthompson.danray.structures;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shading.fullspectrum.FullSpectralPowerDistribution;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by daniel on 1/1/15.
 */
public class Sample {

   public static final AtomicLong instances = new AtomicLong();

   public SpectralPowerDistribution SpectralPowerDistribution;
   public final float x, y;
   public int KDHeatCount;

   public Sample(final float x, final float y) {
      this.x = x;
      this.y = y;
      instances.incrementAndGet();
   }

   @Override
   public String toString() {

      float sum = 0;

      if (SpectralPowerDistribution != null) {
         sum = SpectralPowerDistribution.magnitude();
      }

      return "Total: " + sum;
   }
}
