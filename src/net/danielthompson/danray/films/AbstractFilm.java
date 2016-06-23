package net.danielthompson.danray.films;

import net.danielthompson.danray.structures.Sample;
import net.danielthompson.danray.utility.GeometryCalculations;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by daniel on 5/13/16.
 */
public abstract class AbstractFilm {
   public final float FilmSpeed = 100.0f;
   public final BufferedImage Image;
   public final float[][] Weights;
   public int Width;
   public int Height;

   public AbstractFilm(BufferedImage image) {
      Image = image;
      Width = image.getWidth();
      Height = image.getHeight();
      Weights = new float[image.getWidth()][image.getHeight()];
   }

   protected void AddSampleToPixel(int x, int y, Sample sample, float weight) {

      float existingImageWeight = Weights[x][y];

      Weights[x][y] += weight;

      float sumWeight = existingImageWeight + weight;

      existingImageWeight /= sumWeight;
      weight /= sumWeight;

      Color existingImageColor = new Color(Image.getRGB(x, y));

      int newR = (int) GeometryCalculations.Lerp(existingImageColor.getRed(), existingImageWeight, sample.Color.getRed(), weight);
      int newG = (int) GeometryCalculations.Lerp(existingImageColor.getGreen(), existingImageWeight, sample.Color.getGreen(), weight);
      int newB = (int) GeometryCalculations.Lerp(existingImageColor.getBlue(), existingImageWeight, sample.Color.getBlue(), weight);

      Color finalColor = new Color(newR, newG, newB);
      Image.setRGB(x, y, finalColor.getRGB());
   }

   public abstract void AddSamples(float x, float y, Sample[] samples);

   public boolean AboveThreshhold() {
      return true;
   }

   public Color RenderPixel(int x, int y) {
      return null;
   }
}