package net.danielthompson.danray.scenes.skyboxes;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Vector3;

import java.awt.*;

public class ColorSkybox extends AbstractSkybox {

   private SpectralPowerDistribution _spd;

   public ColorSkybox(Color c) {
      _spd = new SpectralPowerDistribution(c);
   }

   public ColorSkybox(SpectralPowerDistribution spd) {
      _spd = new SpectralPowerDistribution(spd);
   }

   @Override
   public SpectralPowerDistribution getSkyBoxSPD(Vector3 direction) {
      return _spd;
   }
}
