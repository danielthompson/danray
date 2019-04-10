package net.danielthompson.danray.textures;

import net.danielthompson.danray.shading.ReflectanceSpectrum;

public class CheckerboardTexture extends AbstractTexture {
   public int UScale = 1;
   public int VScale = 1;
   public ReflectanceSpectrum Odd;
   public ReflectanceSpectrum Even;

   @Override
   public ReflectanceSpectrum Evaluate(float u, float v) {
      int u1 = (int)(Math.floor(UScale * u)) % 2;
      int v1 = (int)(Math.floor(VScale * v)) % 2;

      return (u1 == v1) ? Even : Odd;
   }
}
