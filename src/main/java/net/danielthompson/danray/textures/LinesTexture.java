package net.danielthompson.danray.textures;

import net.danielthompson.danray.shading.ReflectanceSpectrum;

public class LinesTexture extends AbstractTexture {
   public int UScale = 1;
   public int VScale = 1;
   public float Thickness = 0.01f;
   public ReflectanceSpectrum Line;
   public ReflectanceSpectrum Background;

   @Override
   public ReflectanceSpectrum Evaluate(float u, float v) {
      float u1 = UScale * u % 1.0f;
      float v1 = VScale * v % 1.0f;

      return (u1 < Thickness || v1 < Thickness) ? Line : Background;
   }
}
