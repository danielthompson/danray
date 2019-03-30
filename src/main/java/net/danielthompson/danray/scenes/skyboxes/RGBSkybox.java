package net.danielthompson.danray.scenes.skyboxes;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Vector;

public class RGBSkybox extends AbstractSkybox {
   @Override
   public SpectralPowerDistribution getSkyBoxSPD(Vector direction) {

      float factor = 1.0f;

      float r = (direction.X + 1.0f) * factor;
      float g = (direction.Y + 1.0f) * factor;
      float b = (direction.Z + 1.0f) * factor;

      return new SpectralPowerDistribution(r, g, b);
   }
}
