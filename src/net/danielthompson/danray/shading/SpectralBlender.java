package net.danielthompson.danray.shading;

import com.sun.xml.internal.bind.api.impl.NameConverter;
import net.danielthompson.danray.structures.Matrix4x4;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;

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

   public static Color Convert(SpectralPowerDistribution spd) {
      float triX = spd.apply(StandardObserverColorMatchingFunction.XBar) * StandardObserverColorMatchingFunction.OneOverYBarSum;
      float triY = spd.apply(StandardObserverColorMatchingFunction.YBar) * StandardObserverColorMatchingFunction.OneOverYBarSum;
      float triZ = spd.apply(StandardObserverColorMatchingFunction.ZBar) * StandardObserverColorMatchingFunction.OneOverYBarSum;

      return ConvertTristumulus(triX, triY, triZ);

   }

   public static Color ConvertTristumulus(float X, float Y, float Z) {

      ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_CIEXYZ);

      float minX = cs.getMinValue(0);
      float maxX = cs.getMaxValue(0);
      float minY = cs.getMinValue(1);
      float maxY = cs.getMaxValue(1);
      float minZ = cs.getMinValue(2);
      float maxZ = cs.getMaxValue(2);

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
      
      float rgb[] = cs.toRGB(xyz);

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
