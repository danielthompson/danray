package net.danielthompson.danray.scenes.skyboxes;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Vector;

public abstract class AbstractSkybox {
   public abstract SpectralPowerDistribution getSkyBoxSPD(Vector direction);
}
