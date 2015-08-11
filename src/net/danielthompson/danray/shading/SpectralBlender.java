package net.danielthompson.danray.shading;

import net.danielthompson.danray.structures.Matrix4x4;
import net.danielthompson.danray.structures.Transform;

import java.awt.*;
import java.awt.color.ColorSpace;

/**
 * Created by daniel on 5/7/15.
 */
public class SpectralBlender {

   public static float FilmSpeed = 100;

   public static float FilmSpeedMultiplier = FilmSpeed / 100f;

   public static void setFilmSpeed(float speed) {
      FilmSpeed = speed;
      FilmSpeedMultiplier = FilmSpeed / 100f;
   }

   private static ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_CIEXYZ);

   private static float minX = cs.getMinValue(0);
   private static float maxX = cs.getMaxValue(0);
   private static float minY = cs.getMinValue(1);
   private static float maxY = cs.getMaxValue(1);
   private static float minZ = cs.getMinValue(2);
   private static float maxZ = cs.getMaxValue(2);

   public static Color ConvertSPDtoRGB(SpectralPowerDistribution spd) {
      float[] xyz = ConvertSPDToXYZ(spd);
      return ConvertXYZtoRGB(xyz[0], xyz[1], xyz[2], null);
   }

   public static float[] ConvertSPDToXYZ(SpectralPowerDistribution spd) {
      float triX = spd.apply(StandardObserverColorMatchingFunction.XBar) * StandardObserverColorMatchingFunction.OneOverYBarSum;
      float triY = spd.apply(StandardObserverColorMatchingFunction.YBar) * StandardObserverColorMatchingFunction.OneOverYBarSum;
      float triZ = spd.apply(StandardObserverColorMatchingFunction.ZBar) * StandardObserverColorMatchingFunction.OneOverYBarSum;

      return new float[] {triX, triY, triZ};
   }

   public static SpectralPowerDistribution BlendWeighted(SpectralPowerDistribution dist1, double weight1, SpectralPowerDistribution dist2, double weight2) {
      SpectralPowerDistribution blend = new SpectralPowerDistribution();

      for (int i = 0; i < dist1.Buckets.length; i++) {
         blend.Buckets[i] = (float)(dist1.Buckets[i] * weight1 + dist2.Buckets[i] * weight2);
      }

      return blend;

   }

   public static float[] BlendXYZ(float[][] colors) {
      float x = 0;
      float y = 0;
      float z = 0;

      for (float[] color : colors) {
         x += color[0];
         y += color[1];
         z += color[2];
      }

      double lengthInverse = 1.0 / (double)colors.length;

      x *= lengthInverse;
      y *= lengthInverse;
      z *= lengthInverse;

      return new float[] {x, y, z};
   }


   public static float[] BlendWeighted(float[] c1, int w1, float[] c2, int w2) {
      float[] result = new float[3];

      float divisor = 1.0f / (float)(w1 + w2);

      result[0] = (c1[0] * w1 + c2[0] * w2) * divisor;
      result[1] = (c1[1] * w1 + c2[1] * w2) * divisor;
      result[2] = (c1[2] * w1 + c2[2] * w2) * divisor;

      return result;
   }

   public static boolean CloseEnough(float[] c1, float[] c2, float percent) {

      float margin = (1.0f - percent);

      float difference = c1[0] - c2[0];
      float absDifference = Math.abs(difference);
      if (absDifference > margin)
         return false;

      difference = c1[1] - c2[1];
      absDifference = Math.abs(difference);
      if (absDifference > margin)
         return false;

      difference = c1[2] - c2[2];
      absDifference = Math.abs(difference);
      if (absDifference > margin)
         return false;

      return true;
   }

   public static boolean CloseEnough(SpectralPowerDistribution spd1, SpectralPowerDistribution spd2, float percent) {

      float margin = (1.0f - percent);

      for (int i = 0; i < spd1.Buckets.length; i++) {

         float ratio = spd1.Buckets[i] / spd2.Buckets[i];

         if (ratio > 1)
            ratio = (1 - (ratio - 1));

         if (ratio < percent)

            return false;

      }

      return true;
   }

   public static Color ConvertXYZtoRGB(float X, float Y, float Z, ColorSpace colorSpace) {

      float min = 0;
      float max = 1.9999695f;

      float var_X = X ; // triX from 0 to  95.047      (Observer = 2�, Illuminant = D65)
      float var_Y = (float)(Y); // triY from 0 to 100.000
      float var_Z = (float)(Z); // triZ from 0 to 108.883

      if (var_X < min)
         var_X = min;
      else if (var_X > max)
         var_X = max;


      if (var_Y < min)
         var_Y = min;

      else if (var_Y > max) {
         var_X = max;
         var_Y = max;
         var_Z = max;
      }

      if (var_Z < min)
         var_Z = min;
      else if (var_Z > max)
         var_Z = max;


      var_X *= 47.52422474442735;
      var_Y *= 50.0007625116283;
      var_Z *= 54.44233024553624;

      float var_R = var_X *  3.2406f + var_Y * -1.5372f + var_Z * -0.4986f;
      float var_G = var_X * -0.9689f + var_Y *  1.8758f + var_Z *  0.0415f;
      float var_B = var_X *  0.0557f + var_Y * -0.2040f + var_Z *  1.0570f;

      var_R = correct(var_R);
      var_G = correct(var_G);
      var_B = correct(var_B);

      int R = (int)(var_R * 255);
      int G = (int)(var_G * 255);
      int B = (int)(var_B * 255);

      if (R > 255) R = 255;
      if (G > 255) G = 255;
      if (B > 255) B = 255;

      if (R < 0) R = 0;
      if (G < 0) G = 0;
      if (B < 0) B = 0;

      return new Color((int)R, (int)G, (int)B);
   }

   public static Color ConvertXYZtoRGBInternal(float X, float Y, float Z, ColorSpace colorSpace) {

      X *= FilmSpeedMultiplier;
      Y *= FilmSpeedMultiplier;
      Z *= FilmSpeedMultiplier;

      float xyz[] = new float[3];

      if (X < minX)
         xyz[0] = minX;
      else if (X > maxX)
         xyz[0] = maxX;
      else
         xyz[0] = X;

      if (Y < minY)
         xyz[1] = minY;
      else if (Y > maxY) {
         xyz[0] = maxX;
         xyz[1] = maxY;
         xyz[2] = maxZ;
      }
      else
         xyz[1] = Y;

      if (Z < minZ)
         xyz[2] = minZ;
      else if (Z > maxZ)
         xyz[2] = maxZ;
      else
         xyz[2] = Z;

      float rgb[];

      if (colorSpace == null) {
         rgb = cs.toRGB(xyz);
      }
      else {
         rgb = colorSpace.toRGB(xyz);
      }

      float R = rgb[0] * 255;
      float G = rgb[1] * 255;
      float B = rgb[2] * 255;

/*
      float var_X = X * FilmSpeedMultiplier; // triX from 0 to  95.047      (Observer = 2�, Illuminant = D65)
      float var_Y = Y * FilmSpeedMultiplier; // triY from 0 to 100.000
      float var_Z = Z * FilmSpeedMultiplier; // triZ from 0 to 108.883

      float var_R = var_X *  3.2406f + var_Y * -1.5372f + var_Z * -0.4986f;
      float var_G = var_X * -0.9689f + var_Y *  1.8758f + var_Z *  0.0415f;
      float var_B = var_X *  0.0557f + var_Y * -0.2040f + var_Z *  1.0570f;

      var_R = correct(var_R);
      var_G = correct(var_G);
      var_B = correct(var_B);

      int R = (int)(var_R * 255);
      int G = (int)(var_G * 255);
      int B = (int)(var_B * 255);

      if (R > 255) R = 255;
      if (G > 255) G = 255;
      if (B > 255) B = 255;

      if (R < 0) R = 0;
      if (G < 0) G = 0;
      if (B < 0) B = 0;
*/
      return new Color((int)R, (int)G, (int)B);
   }

   private static float correct(float value) {

      if ( value > 0.0031308 )
         value = 1.055f * (float)Math.pow(value, (1.0f /2.4f)) - 0.055f;
      else
         value = 12.92f * value;

      return value;
   }

   public static Transform XYZtoRGBTransform = new Transform(new Matrix4x4(
          3.240479, -1.537150, -0.498535, 0,
         -0.969256,  1.875992,  0.041556, 0,
          0.055648, -0.204043,  1.057311, 0,
          0,         0,         0,        1));
}
