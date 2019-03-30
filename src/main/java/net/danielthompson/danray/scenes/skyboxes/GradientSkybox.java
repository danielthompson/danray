package net.danielthompson.danray.scenes.skyboxes;

import net.danielthompson.danray.shading.Blender;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Vector;

import java.awt.*;

public class GradientSkybox extends AbstractSkybox {

   private Color _c1;
   private Color _c2;

   /**
    * Creates a new gradient skybox from the two given colors.
    * @param c1 Color at highest y (the "sky").
    * @param c2 Color at lowest y (the "ground").
    */
   public GradientSkybox(Color c1, Color c2) {
      _c1 = c1;
      _c2 = c2;
   }

   @Override
   public SpectralPowerDistribution getSkyBoxSPD(Vector direction) {
      // uses mathematics convention:
      // r = radial distance (if we assume direction is normalized, which we do, this is 1)
      // θ (theta) = azimuthal angle - angle in x-z plane
      // φ (phi) = polar angle - angle in the θ-z plane

      float phi = (float)Math.acos(direction.Y);

      float factor = (1.0f - Constants.OneOverPi * phi);

      Color blended = Blender.BlendWeighted(_c1, factor, _c2, 1.0f - factor);

      return new SpectralPowerDistribution(blended);
   }
}
