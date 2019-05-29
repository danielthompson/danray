package net.danielthompson.danray.films;

import net.danielthompson.danray.shading.Spectrum;
import net.danielthompson.danray.structures.Sample;
import net.danielthompson.danray.utility.GeometryCalculations;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by daniel on 5/7/16.
 */
public class BoxFilterFilm extends AbstractFilm {

   public BoxFilterFilm(final BufferedImage image)  {
      super(image);
   }

   @Override
   public void AddSamples(final float x, final float y, final Sample[] samples) {
      final int xFloor = (int)x;
      final int yFloor = (int)y;

      Weights[xFloor][yFloor] += samples.length;

      final float oneOverSamples = 1.0f / Weights[xFloor][yFloor];

      if (Samples[xFloor][yFloor] == null)
         Samples[xFloor][yFloor] = new Spectrum();

      for (final Sample sample : samples) {
         sample.SpectralPowerDistribution.clamp();
         Samples[xFloor][yFloor].add(sample.SpectralPowerDistribution);
      }

      final float r = Samples[xFloor][yFloor].R * oneOverSamples;
      final float g = Samples[xFloor][yFloor].G * oneOverSamples;
      final float b = Samples[xFloor][yFloor].B * oneOverSamples;

      final float clampedR = clamp(0.0f, r, 1.0f);
      final float clampedG = clamp(0.0f, g, 1.0f);
      final float clampedB = clamp(0.0f, b, 1.0f);

      final Color newSampleColor = new Color(clampedR, clampedG, clampedB);

      Image.setRGB(xFloor, yFloor, newSampleColor.getRGB());
   }
}
