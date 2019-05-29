package net.danielthompson.danray.textures;

import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.structures.Constants;

import java.awt.*;

public class ConstantTexture extends AbstractTexture {

   public final ReflectanceSpectrum ReflectanceSpectrum;

   public ConstantTexture(Color c) {
      ReflectanceSpectrum = new ReflectanceSpectrum(c.getRed() * Constants.OneOver255f, c.getGreen() * Constants.OneOver255f, c.getBlue() * Constants.OneOver255f);
   }

   public ConstantTexture(ReflectanceSpectrum reflectanceSpectrum) {
      ReflectanceSpectrum = reflectanceSpectrum;
   }

   @Override
   public ReflectanceSpectrum Evaluate(final float u, final float v) {
      return ReflectanceSpectrum;
   }
}
