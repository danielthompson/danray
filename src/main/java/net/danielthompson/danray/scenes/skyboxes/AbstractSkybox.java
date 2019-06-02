package net.danielthompson.danray.scenes.skyboxes;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Vector3;

public abstract class AbstractSkybox {
   public abstract SpectralPowerDistribution getSPD(Vector3 direction);
}
