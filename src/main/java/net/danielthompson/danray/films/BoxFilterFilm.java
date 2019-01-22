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

      float newSampleWeight = samples.length;

      int xFloor = (int)x;
      int yFloor = (int)y;

      Spectrum spectrum = new Spectrum();

      for (Sample sample : samples) {
         spectrum.R += (sample.SpectralPowerDistribution.R);
         spectrum.G += (sample.SpectralPowerDistribution.G);
         spectrum.B += (sample.SpectralPowerDistribution.B);
      }

      float R = spectrum.R / newSampleWeight;
      float G = spectrum.G / newSampleWeight;
      float B = spectrum.B / newSampleWeight;

      float clampedR = clamp(0.0f, R, 1.0f);
      float clampedG = clamp(0.0f, G, 1.0f);
      float clampedB = clamp(0.0f, B, 1.0f);

      Color newSampleColor = new Color(clampedR, clampedG, clampedB);

      float existingImageWeight = Weights[xFloor][yFloor];

      float weightsum = existingImageWeight + newSampleWeight;

      float normalizedExistingImageWeight = existingImageWeight / weightsum;
      float normalizedNewSampleWeight = newSampleWeight / weightsum;

      Color existingImageColor = new Color(Image.getRGB(xFloor, yFloor));

      int newR = (int)GeometryCalculations.Lerp(existingImageColor.getRed(), normalizedExistingImageWeight, newSampleColor.getRed(), normalizedNewSampleWeight);
      int newG = (int)GeometryCalculations.Lerp(existingImageColor.getGreen(), normalizedExistingImageWeight, newSampleColor.getGreen(), normalizedNewSampleWeight);
      int newB = (int)GeometryCalculations.Lerp(existingImageColor.getBlue(), normalizedExistingImageWeight, newSampleColor.getBlue(), normalizedNewSampleWeight);

      Color finalColor = new Color(newR, newG, newB);
      Image.setRGB(xFloor, yFloor, finalColor.getRGB());

      Weights[xFloor][yFloor] = weightsum;

   }

}
