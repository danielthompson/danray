package net.danielthompson.danray.shading;

import java.awt.*;

import static net.danielthompson.danray.structures.Constants.*;

/**
 * Created by daniel on 5/7/16.
 */
public class RGBSpectrum {
   public float R;
   public float G;
   public float B;


   public RGBSpectrum() {

   }

   public RGBSpectrum(Color c) {
      R = c.getRed() * OneOver255f;
      G = c.getGreen() * OneOver255f;
      B = c.getBlue() * OneOver255f;
   }
}
