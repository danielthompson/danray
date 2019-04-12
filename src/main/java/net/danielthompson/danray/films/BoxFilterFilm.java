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

   public BoxFilterFilm(BufferedImage image)  {
      super(image);
   }

   @Override
   public void AddSamples(float x, float y, Sample[] samples) {

      int xFloor = (int)x;
      int yFloor = (int)y;

      Weights[xFloor][yFloor] += samples.length;

      float oneOverSamples = 1.0f / Weights[xFloor][yFloor];

      if (Samples[xFloor][yFloor] == null)
         Samples[xFloor][yFloor] = new Spectrum();

      for (Sample sample : samples) {
         Samples[xFloor][yFloor].R += (sample.SpectralPowerDistribution.R);
         Samples[xFloor][yFloor].G += (sample.SpectralPowerDistribution.G);
         Samples[xFloor][yFloor].B += (sample.SpectralPowerDistribution.B);
      }

      float r = Samples[xFloor][yFloor].R * oneOverSamples;
      float g = Samples[xFloor][yFloor].G * oneOverSamples;
      float b = Samples[xFloor][yFloor].B * oneOverSamples;

      float clampedR = (float)clamp(0.0f, r, 1.0f);
      float clampedG = (float)clamp(0.0f, g, 1.0f);
      float clampedB = (float)clamp(0.0f, b, 1.0f);

      Color newSampleColor = new Color(clampedR, clampedG, clampedB);

      Image.setRGB(xFloor, yFloor, newSampleColor.getRGB());
   }
}
