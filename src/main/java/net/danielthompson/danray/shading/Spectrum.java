package net.danielthompson.danray.shading;

import java.awt.Color;

import static net.danielthompson.danray.structures.Constants.*;

/**
 * Created by daniel on 5/7/16.
 */
public class Spectrum {
   public float R;
   public float G;
   public float B;


   public Spectrum() {

   }

   public Spectrum(float r, float g, float b) {
      R = r;
      G = g;
      B = b;
   }

   public void add(Spectrum s1) {
      R += s1.R;
      G += s1.G;
      B += s1.B;
   }

   public float sum() {
      return R + G + B;
   }

   public Spectrum(Color c) {
      R = c.getRed() * OneOver255f;
      G = c.getGreen() * OneOver255f;
      B = c.getBlue() * OneOver255f;
   }

   public Spectrum(Color c, float power) {
      R = c.getRed() * OneOver255f * power;
      G = c.getGreen() * OneOver255f * power;
      B = c.getBlue() * OneOver255f * power;
   }

   public static Spectrum Lerp(Spectrum s1, float w1, Spectrum s2, float w2) {
      Spectrum s = new Spectrum();

      s.R = s1.R * w1 + s2.R * w2;
      s.G = s1.G * w1 + s2.G * w2;
      s.B = s1.B * w1 + s2.B * w2;

      return s;
   }
}
