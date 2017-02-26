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

   private float clamp(float var0, float var1, float var2) {
      return var1 < var0?var0:(var1 > var2?var2:var1);
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

      R = clamp(0.0f, R, 1.0f);
      G = clamp(0.0f, G, 1.0f);
      B = clamp(0.0f, B, 1.0f);

      Color newSampleColor = new Color(R, G, B);

      float existingImageWeight = Weights[xFloor][yFloor];

      float maxWeight = Math.max(newSampleWeight, existingImageWeight);

      newSampleWeight /= maxWeight;
      existingImageWeight /= maxWeight;

      Color existingImageColor = new Color(Image.getRGB(xFloor, yFloor));

      int newR = (int)GeometryCalculations.Lerp(existingImageColor.getRed(), existingImageWeight, newSampleColor.getRed(), newSampleWeight);
      int newG = (int)GeometryCalculations.Lerp(existingImageColor.getGreen(), existingImageWeight, newSampleColor.getGreen(), newSampleWeight);
      int newB = (int)GeometryCalculations.Lerp(existingImageColor.getBlue(), existingImageWeight, newSampleColor.getBlue(), newSampleWeight);

      Color finalColor = new Color(newR, newG, newB);
      Image.setRGB(xFloor, yFloor, finalColor.getRGB());
   }

}
