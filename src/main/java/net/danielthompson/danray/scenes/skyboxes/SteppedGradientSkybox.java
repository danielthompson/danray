package net.danielthompson.danray.scenes.skyboxes;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Vector3;

import java.awt.*;
import java.util.List;


public class SteppedGradientSkybox extends AbstractSkybox {

   private List<Color> _colors;
   private List<Float> _locations;

   /**
    * Creates a new gradient skybox from the two given colors.
    * @param colors Color at highest y (the "sky").
    * @param locations Color at lowest y (the "ground").
    */
   public SteppedGradientSkybox(List<Color> colors, List<Float> locations) {
      _colors = colors;
      _locations = locations;
   }

   @Override
   public SpectralPowerDistribution getSPD(Vector3 direction) {
      // uses mathematics convention:
      // r = radial distance (if we assume direction is normalized, which we do, this is 1)
      // θ (theta) = azimuthal angle - angle in x-z plane
      // φ (phi) = polar angle - angle in the θ-z plane

      float phi = (float)Math.acos(direction.y);

      float location = (1.0f - Constants.OneOverPi * phi);

      for (int i = 0; i < _locations.size() - 1; i++) {
         float first = _locations.get(i);
         float second = _locations.get(i + 1);

         if (location <= first)
            return new SpectralPowerDistribution(_colors.get(i));

         if (location <= second) {
            float w1 = Math.abs(location - first);
            float w2 = Math.abs(location - second);
            float normalizationFactor = 1.0f / (w1 + w2);

            w1 *= normalizationFactor;
            w2 *= normalizationFactor;

            SpectralPowerDistribution spd1 = new SpectralPowerDistribution(_colors.get(i));
            SpectralPowerDistribution spd2 = new SpectralPowerDistribution(_colors.get(i + 1));
            SpectralPowerDistribution lerped = SpectralPowerDistribution.Lerp(spd1, w2, spd2, w1);
            return lerped;
         }
      }

      return new SpectralPowerDistribution(_colors.get(_colors.size() - 1));
   }
}
