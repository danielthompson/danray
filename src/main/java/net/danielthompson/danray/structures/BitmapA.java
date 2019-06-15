package net.danielthompson.danray.structures;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.BitSet;

public class BitmapA {
   public final int[] rgb;
   public final int width;
   public final int height;

   public BitmapA(final int width, final int height) {
      this.width = width;
      this.height = height;
      final int total = width * height;
      rgb = new int[total];
   }

   public BitmapA(final BufferedImage image) {
      width = image.getWidth();
      height = image.getHeight();

      final int total = width * height;
      rgb = new int[total];
      //      final int firstByte = rgb & 0x000000FF; // blue
      //      final int secondByte = (rgb & 0x0000FF00) >> 8; // green
      //      final int thirdByte = (rgb & 0x00FF0000) >> 16; // red
      //      final int fourthByte = (rgb & 0xFF000000) >> 24; // alpha

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            final int rgbValue = image.getRGB(x, y);

            set(x, y, rgbValue);
            int alpha = getAlpha(x, y);
         }
      }
   }

   public void set(int x, int y, int rgb) {
      final int index = y * width + x;
      this.rgb[index] = rgb;
   }

   public void set(int x, int y, int r, int g, int b) {
      final int index = y * width + x;
      final int rgb =             0xFF000000 |
            ((r << 16) & 0x00FF0000) |
            ((g << 8) & 0x0000FF00) |
            (b & 0x000000FF);
      this.rgb[index] = rgb;
   }


   public int getAlpha(final int x, final int y) {
      final int index = y * width + x;
      return getAlpha(rgb[index]);
   }

   public int getAlpha(final int color) {
      return (color & 0xFF000000) >>> 24;
   }

   public int getRed(final int color) {
      return (color & 0x00FF0000) >> 16;
   }

   public int getRed(final int x, final int y) {
      final int index = y * width + x;
      return (getRed(rgb[index]));
   }

   public int getGreen(final int color) {
      return (color & 0x0000FF00) >> 8;
   }

   public int getGreen(final int x, final int y) {
      final int index = y * width + x;
      return (getGreen(rgb[index]));
   }

   public int getBlue(final int color) {
      return (color & 0x000000FF);
   }

   public int getBlue(final int x, final int y) {
      final int index = y * width + x;
      return (getBlue(rgb[index]));
   }

   public void set(final BufferedImage image, final int x, final int y, final boolean transpose) {
      final int sourceXBegin = 0;
      final int sourceXEnd = image.getWidth();

      final int sourceYBegin = 0;
      final int sourceYEnd = image.getHeight();

      for (int row = sourceYBegin; row < sourceYEnd; row++) {
         for (int col = sourceXBegin; col < sourceXEnd; col++) {
            final int color = image.getRGB(col, row);
            final int destX = (transpose ? row : col) + x;
            final int destY = (transpose ? col : row) + y;
            set(destX, destY, color);
         }
      }
   }

   public BufferedImage export() {
      BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_4BYTE_ABGR);
      for (int x = 0; x < this.width; x++) {
         for (int y = 0; y < this.height; y++) {
            final int index = y * width + x;
            final int rgbValue = this.rgb[index];
            Color c = new Color(rgbValue);
            image.setRGB(x, y, rgbValue);
         }
      }

      return image;
   }

   public int average(int x, int y, int n) {
      int r = 0, g = 0, b = 0;
      int numValues = 0;
      for (int i = x; i < x + n; i++) {
         for (int j = y; j < y + n; j++) {
            if (getAlpha(i, j) == 255) {
               r += getRed(i, j);
               g += getGreen(i, j);
               b += getBlue(i, j);
               numValues++;
            }
         }
      }
      r /= numValues;
      g /= numValues;
      b /= numValues;

      final int averageColor =
            0xFF000000 |
            ((r << 16) & 0x00FF0000) |
            ((g << 8) & 0x0000FF00) |
            (b & 0x000000FF);

      return averageColor;

   }
}
