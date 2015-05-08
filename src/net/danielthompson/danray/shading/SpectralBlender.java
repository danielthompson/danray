package net.danielthompson.danray.shading;

import com.sun.xml.internal.bind.api.impl.NameConverter;
import net.danielthompson.danray.structures.Matrix4x4;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;

import java.awt.*;

/**
 * Created by daniel on 5/7/15.
 */
public class SpectralBlender {
   public static Color Convert(SpectralPowerDistribution spd) {
      float triX = spd.apply(StandardObserverColorMatchingFunction.XBar);
      float triY = spd.apply(StandardObserverColorMatchingFunction.YBar);
      float triZ = spd.apply(StandardObserverColorMatchingFunction.ZBar);

      Vector xyz = new Vector(triX, triY, triZ);

      Vector rgb = XYZtoRGBTransform.Apply(xyz);

      float r = (float)rgb.X;
      float g = (float)rgb.Y;
      float b = (float)rgb.Z;
      
      if (r < 0) r = 0;
      else if (r > 1) r = 1;
      if (g < 0) g = 0;
      else if (g > 1) g = 1;
      if (b < 0) b = 0;
      else if (b > 1) b = 1;

      return new Color(r, g, b);
   }

   public static Transform XYZtoRGBTransform = new Transform(new Matrix4x4(
          3.240479, -1.537150, -0.498535, 0,
         -0.969256,  1.875992,  0.041556, 0,
          0.055648, -0.204043,  1.057311, 0,
          0,         0,         0,        1));
}
