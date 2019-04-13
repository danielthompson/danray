package net.danielthompson.danray.textures;

import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.states.Intersection;

public class CheckerboardTexture extends AbstractTexture {
   public int UScale = 1;
   public int VScale = 1;
   public ReflectanceSpectrum Odd;
   public ReflectanceSpectrum Even;

   @Override
   public ReflectanceSpectrum Evaluate(Intersection intersection) {
      int u1 = (int)(Math.floor(UScale * intersection.u)) % 2;
      int v1 = (int)(Math.floor(VScale * intersection.v)) % 2;

      return (u1 == v1) ? Even : Odd;
   }
}
