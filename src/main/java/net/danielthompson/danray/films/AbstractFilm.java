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

      if (Samples[x][y] == null)
         Samples[x][y] = new Spectrum();

      Weights[x][y] += weight;
      Samples[x][y].add(SpectralPowerDistribution.scale(sample.SpectralPowerDistribution, weight));

      float r = Samples[x][y].R / Weights[x][y];
      float g = Samples[x][y].G / Weights[x][y];
      float b = Samples[x][y].B / Weights[x][y];

      float clampedR = clamp(0.0f, r, 1.0f);
      float clampedG = clamp(0.0f, g, 1.0f);
      float clampedB = clamp(0.0f, b, 1.0f);

      Color c = new Color(clampedR, clampedG, clampedB);
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