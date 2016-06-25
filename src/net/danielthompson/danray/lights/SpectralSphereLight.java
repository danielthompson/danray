package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.fullspectrum.FullSpectralPowerDistribution;

/**
 * Created by daniel on 5/5/15.
 */

public class SpectralSphereLight extends SphereLight implements SpectralRadiatable {

   public FullSpectralPowerDistribution SPD;

   public SpectralSphereLight(double power) {
      super(power);
   }

   public SpectralSphereLight(double power, Material material) {
      super(power, material);
      Power = power;
   }

   public SpectralSphereLight(double power, Material material, FullSpectralPowerDistribution spd) {
      super(power, material);
      this.SPD = spd;

      if (material == null) {
         this.Material = new Material();

      }
   }

   @Override
   public FullSpectralPowerDistribution getSpectralPowerDistribution() {
      return SPD;
   }

   public String toString() {
      return ID + "";
   }

}
