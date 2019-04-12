package net.danielthompson.danray.films;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shading.Spectrum;
import net.danielthompson.danray.structures.Sample;
import net.danielthompson.danray.utility.GeometryCalculations;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by daniel on 5/13/16.
 */
public abstract class AbstractFilm {
   public final float FilmSpeed = 200.0f;
   public final BufferedImage Image;
   public final float[][] Weights;
   public final Spectrum[][] Samples;
   public int Width;
   public int Height;

   public AbstractFilm(BufferedImage image) {
      Image = image;
      Width = image.getWidth();
      Height = image.getHeight();
      Weights = new float[image.getWidth()][image.getHeight()];
      Samples = new Spectrum[image.getWidth()][image.getHeight()];
   }

   protected void AddSampleToPixel(int x, int y, Sample sample, float weight) {

      float existingImageWeight = Weights[x][y];

      Weights[x][y] += weight;

      float sumWeight = existingImageWeight + weight;

      existingImageWeight /= sumWeight;
      weight /= sumWeight;

      SpectralPowerDistribution existingSPD = new SpectralPowerDistribution(new Color(Image.getRGB(x, y)));

      SpectralPowerDistribution finalColor = SpectralPowerDistribution.Lerp(existingSPD, existingImageWeight, sample.SpectralPowerDistribution, weight);

      Color c = new Color(finalColor.R, finalColor.G, finalColor.B);

      Image.setRGB(x, y, c.getRGB());
   }

   protected float clamp(float var0, float var1, float var2) {
      return var1 < var0 ? var0 : (var1 > var2 ? var2 : var1);
   }

   public abstract void AddSamples(float x, float y, Sample[] samples);

   public boolean AboveThreshhold() {
      return true;
   }

   public Color RenderPixel(int x, int y) {
      return null;
   }
}