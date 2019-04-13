package net.danielthompson.danray.textures;

import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.states.Intersection;

public class Checkerboard3DTexture extends AbstractTexture {
   public int XScale = 1;
   public int YScale = 1;
   public int ZScale = 1;
   public ReflectanceSpectrum Odd;
   public ReflectanceSpectrum Even;

   @Override
   public ReflectanceSpectrum Evaluate(Intersection intersection) {
      int x = (int)Math.abs((intersection.Location.X / XScale) % 2);
      int y = (int)Math.abs((intersection.Location.Y / YScale) % 2);
      int z = (int)Math.abs((intersection.Location.Z / ZScale) % 2);

      boolean xy = (x == y) && (x != z);
      boolean xz = (x == z) && (x != y);
      boolean yz = (y == z) && (x != y);

      return (xy || xz || yz) ? Even : Odd;
   }
}
