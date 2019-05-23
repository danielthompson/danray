package net.danielthompson.danray.scenes.skyboxes;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Vector3;

public class RGBSkybox extends AbstractSkybox {
   @Override
   public SpectralPowerDistribution getSkyBoxSPD(Vector3 direction) {

      float factor = 1.0f;

      float r = (direction.x + 1.0f) * factor;
      float g = (direction.y + 1.0f) * factor;
      float b = (direction.z + 1.0f) * factor;

      return new SpectralPowerDistribution(r, g, b);
   }
}
