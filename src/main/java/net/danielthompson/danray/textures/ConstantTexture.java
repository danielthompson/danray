package net.danielthompson.danray.textures;

import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.states.Intersection;

public class ConstantTexture extends AbstractTexture {

   public final ReflectanceSpectrum ReflectanceSpectrum;

   public ConstantTexture(ReflectanceSpectrum reflectanceSpectrum) {
      ReflectanceSpectrum = reflectanceSpectrum;
   }

   @Override
   public ReflectanceSpectrum Evaluate(Intersection intersection) {
      return ReflectanceSpectrum;
   }
}
