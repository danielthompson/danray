package net.danielthompson.danray.shading;

import java.awt.*;

/**
 * DanRay
 * User: dthompson
 * Date: 7/8/13
 * Time: 1:49 PM
 */
public class Blender {

   public static Color BlendHSB(Color reflectedColor, Color diffuseObjectColor, float reflectivity) {

      float[] diffuseHSB = Color.RGBtoHSB(diffuseObjectColor.getRed(), diffuseObjectColor.getGreen(), diffuseObjectColor.getBlue(), null);
      float[] reflectedHSB = Color.RGBtoHSB(reflectedColor.getRed(), reflectedColor.getGreen(), reflectedColor.getBlue(), null);

      float[] newHSB = new float[3];

      for (int i = 0; i < newHSB.length; i++) {
         newHSB[i] = reflectivity * reflectedHSB[i] + (1.0f - reflectivity) * diffuseHSB[i];
      }

      Color blendedColor = Color.getHSBColor(newHSB[0], newHSB[1], newHSB[2]);
      return blendedColor;
   }

   public static Color BlendRGB(Color reflectedColor, Color diffuseObjectColor, float reflectivity) {

      float[] diffuseRGB = diffuseObjectColor.getRGBColorComponents(null);
      float[] reflectedRGB = reflectedColor.getRGBColorComponents(null);

      float[] newRGB = new float[3];

      for (int i = 0; i < newRGB.length; i++) {
         newRGB[i] = reflectivity * reflectedRGB[i] + (1.0f - reflectivity) * diffuseRGB[i];
      }

      Color blendedColor = new Color(newRGB[0], newRGB[1], newRGB[2]);
      return blendedColor;
   }

   public static Color BlendRGB(Color[] colors, float[] weights) {

      Normalize(weights);

      float[] newRGB = new float[3];

      for (int n = 0; n < colors.length; n++) {
         if (colors[n] != null) {
            float[] colorRGB = colors[n].getRGBColorComponents(null);
            for (int i = 0; i < newRGB.length; i++) {
               newRGB[i] += colorRGB[i] * weights[n];
            }
         }
      }

      Color blendedColor = new Color(newRGB[0], newRGB[1], newRGB[2]);
      return blendedColor;
   }

   private static void Normalize(float[] weights) {
      float max = 0;

      for (int n = 0; n < weights.length; n++) {
         if (weights[n] > max)
            max = weights[n];
      }

      float multiplicand = 1.f / max;

      for (int n = 0; n < weights.length; n++) {
         weights[n] *= multiplicand;
      }
   }

   public static Color BlendRGB(Color diffuseObjectColor, Color reflectedColor, float reflectivity, Color refractedColor, float transparency) {

      float[] diffuseRGB = diffuseObjectColor.getRGBColorComponents(null);
      float[] reflectedRGB = reflectedColor.getRGBColorComponents(null);
      float[] refractedRGB = refractedColor.getRGBColorComponents(null);

      float[] newRGB = new float[3];

      for (int i = 0; i < newRGB.length; i++) {
         newRGB[i] = (1.0f - reflectivity - transparency) * diffuseRGB[i] + reflectivity * reflectedRGB[i] + transparency * refractedRGB[i];
      }

      Color blendedColor = new Color(newRGB[0], newRGB[1], newRGB[2]);
      return blendedColor;
   }

   public static Color BlendRGBLighter(Color reflectedColor, Color diffuseObjectColor, float reflectivity) {

      float[] diffuseRGB = diffuseObjectColor.getRGBColorComponents(null);
      float[] reflectedRGB = reflectedColor.getRGBColorComponents(null);

      float[] newRGB = new float[3];

      newRGB[0] = reflectivity * reflectedRGB[0] + (1.0f - reflectivity) * diffuseRGB[0];
      newRGB[1] = reflectivity * reflectedRGB[1] + (1.0f - reflectivity) * diffuseRGB[1];
      newRGB[2] = reflectivity * reflectedRGB[2] + (1.0f - reflectivity) * diffuseRGB[2];

      Color blendedColor = new Color(newRGB[0], newRGB[1], newRGB[2]);
      return blendedColor;
   }

   public static Color BlendColors(Color[] colors) {
      int r = 0;
      int g = 0;
      int b = 0;

      for (Color color : colors) {
         r += color.getRed();
         g += color.getGreen();
         b += color.getBlue();
      }

      double lengthInverse = 1.0 / (double)colors.length;

      r *= lengthInverse;
      g *= lengthInverse;
      b *= lengthInverse;

      return new Color(r, g, b);
   }

   public static boolean CloseEnough(Color c1, Color c2, float percent) {
      float[] color1 = c1.getRGBColorComponents(null);
      float[] color2 = c2.getRGBColorComponents(null);

      float margin = (1.0f - percent);

      for (int i = 0; i < color1.length; i++) {
         float difference = color1[i] - color2[i];
         float absDifference = Math.abs(difference);
         boolean value = absDifference > margin;

         if (value)
            return false;
      }

      return true;
   }

   public static boolean CloseEnoughFast(Color c1, Color c2, float percent) {
      int color1 = c1.getRed() * 65536;
      color1 += c1.getGreen() * 255;
      color1 += c1.getBlue();

      int color2 = c2.getRed() * 65536;
      color2 += c2.getGreen() * 255;
      color2 += c2.getBlue();

      return true;
   }

   public static Color BlendWeighted(Color c1, int w1, Color c2, int w2) {
      float[] color1 = c1.getRGBColorComponents(null);
      float[] color2 = c2.getRGBColorComponents(null);

      float[] result = new float[3];

      float divisor = 1.0f / (float)(w1 + w2);

      for (int i = 0; i < color1.length; i++) {
         float weightedC1 = (color1[i] * (float)w1);
         float weightedC2 = (color2[i] * (float)w2);

         result[i] = divisor * (weightedC1 + weightedC2);
      }

      return new Color(result[0], result[1], result[2]);

   }
}
