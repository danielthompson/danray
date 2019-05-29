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

   public AbstractFilm(final BufferedImage image) {
      Image = image;
      Width = image.getWidth();
      Height = image.getHeight();
      Weights = new float[image.getWidth()][image.getHeight()];
      Samples = new Spectrum[image.getWidth()][image.getHeight()];
   }

   protected void AddSampleToPixel(final int x, final int y, final Sample sample, final float weight) {

      if (Samples[x][y] == null)
         Samples[x][y] = new Spectrum();

      SpectralPowerDistribution weighted = SpectralPowerDistribution.scale(sample.SpectralPowerDistribution, weight);

      weighted.R = clamp(0.0f, weighted.R, 1.0f);
      weighted.G = clamp(0.0f, weighted.G, 1.0f);
      weighted.B = clamp(0.0f, weighted.B, 1.0f);

      Samples[x][y].add(weighted);

      Weights[x][y] += weight;

      final float multiplier = 1.0f / Weights[x][y];

      float finalR = Samples[x][y].R * multiplier;
      float finalG = Samples[x][y].G * multiplier;
      float finalB = Samples[x][y].B * multiplier;

      finalR = clamp(0.0f, finalR, 1.0f);
      finalG = clamp(0.0f, finalG, 1.0f);
      finalB = clamp(0.0f, finalB, 1.0f);

      float r = finalR;
      float g = finalG;
      float b = finalB;

      if (r > 1.0f || g > 1.0f || b > 1.0f) {
         int i = 0;
      }


      //float clampedR = clamp(0.0f, r, 1.0f);
      //float clampedG = clamp(0.0f, g, 1.0f);
      //float clampedB = clamp(0.0f, b, 1.0f);

//      Color c = new Color(clampedR, clampedG, clampedB);

      try {
         final Color c = new Color(r, g, b);
         Image.setRGB(x, y, c.getRGB());
      }
      catch (IllegalArgumentException e) {
         int i = 0;
      }
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