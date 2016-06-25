package net.danielthompson.danray.shading;

import java.awt.Color;

/**
 * Created by daniel on 6/25/16.
 */
public class ReflectanceSpectrum extends Spectrum {

   public ReflectanceSpectrum() {

   }

   public ReflectanceSpectrum(float r, float g, float b)  {
      super (r, g, b);
   }

   public ReflectanceSpectrum(Color c) {
      super (c);
   }

}
