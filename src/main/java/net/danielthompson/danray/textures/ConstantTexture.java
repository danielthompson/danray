package net.danielthompson.danray.textures;

import net.danielthompson.danray.shading.ReflectanceSpectrum;

public class ConstantTexture extends AbstractTexture {

   public final ReflectanceSpectrum ReflectanceSpectrum;

   public ConstantTexture(ReflectanceSpectrum reflectanceSpectrum) {
      ReflectanceSpectrum = reflectanceSpectrum;
   }

   @Override
   public ReflectanceSpectrum Evaluate(float u, float v) {
      return ReflectanceSpectrum;
   }
}
