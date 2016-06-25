package net.danielthompson.danray.films;

import net.danielthompson.danray.shading.Spectrum;
import net.danielthompson.danray.structures.Constants;
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
         spectrum.R += (sample.Color.getRed() * Constants.OneOver255f);
         spectrum.G += (sample.Color.getGreen() * Constants.OneOver255f);
         spectrum.B += (sample.Color.getBlue() * Constants.OneOver255f);
      }

      Color newSampleColor = new Color(spectrum.R / newSampleWeight, spectrum.G / newSampleWeight, spectrum.B / newSampleWeight);

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
