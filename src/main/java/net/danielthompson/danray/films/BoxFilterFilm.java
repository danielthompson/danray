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

      if (yFloor == 20) {
         int j = 0;
      }

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

//      float maxWeight = Math.max(newSampleWeight, existingImageWeight);
//      newSampleWeight /= maxWeight;
//      existingImageWeight /= maxWeight;

      float weightsum = existingImageWeight + newSampleWeight;

      float normalizedExistingImageWeight = existingImageWeight / weightsum;
      float normalizedNewSampleWeight = newSampleWeight / weightsum;

      Color existingImageColor = new Color(Image.getRGB(xFloor, yFloor));

      int newR = (int)GeometryCalculations.Lerp(existingImageColor.getRed(), normalizedExistingImageWeight, newSampleColor.getRed(), normalizedNewSampleWeight);
      int newG = (int)GeometryCalculations.Lerp(existingImageColor.getGreen(), normalizedExistingImageWeight, newSampleColor.getGreen(), normalizedNewSampleWeight);
      int newB = (int)GeometryCalculations.Lerp(existingImageColor.getBlue(), normalizedExistingImageWeight, newSampleColor.getBlue(), normalizedNewSampleWeight);

//      if (newR == 255 && newG == 255 && newB == 255) {
//         int highestRIndex = 0;
//         int highestGIndex = 0;
//         int highestBIndex = 0;
//         int highestTotalIndex = 0;
//
//         float highestR = 0.0f;
//         float highestG = 0.0f;
//         float highestB = 0.0f;
//         float highestTotal = 0.0f;
//
//         for (int k = 0; k < samples.length; k++) {
//            Sample sample = samples[k];
//            if (sample.SpectralPowerDistribution.R > highestR) {
//               highestR = sample.SpectralPowerDistribution.R;
//               highestRIndex = k;
//            }
//            if (sample.SpectralPowerDistribution.G > highestG) {
//               highestG = sample.SpectralPowerDistribution.G;
//               highestGIndex = k;
//            }
//            if (sample.SpectralPowerDistribution.B > highestB) {
//               highestB = sample.SpectralPowerDistribution.B;
//               highestBIndex = k;
//            }
//
//            float total = sample.SpectralPowerDistribution.R + sample.SpectralPowerDistribution.G + sample.SpectralPowerDistribution.B;
//
//            if (total > highestTotal) {
//               total = highestTotal;
//               highestTotalIndex = k;
//            }
//
//         }
//
//         int l = 0;
//      }

      Color finalColor = new Color(newR, newG, newB);
      Image.setRGB(xFloor, yFloor, finalColor.getRGB());

      Weights[xFloor][yFloor] = weightsum;

   }

}
