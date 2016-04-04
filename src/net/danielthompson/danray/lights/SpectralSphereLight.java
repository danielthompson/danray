package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.SpectralPowerDistribution;

/**
 * Created by daniel on 5/5/15.
 */

public class SpectralSphereLight extends SphereLight implements SpectralRadiatable {

   public SpectralPowerDistribution SPD;

   public SpectralSphereLight(double power) {
      super(power);
   }

   public SpectralSphereLight(double power, Material material) {
      super(power, material);
      Power = power;
   }

   public SpectralSphereLight(double power, Material material, SpectralPowerDistribution spd) {
      super(power, material);
      this.SPD = spd;

      if (material == null) {
         this.Material = new Material();

      }
   }

   @Override
   public SpectralPowerDistribution getSpectralPowerDistribution() {
      return SPD;
   }

   public String toString() {
      return ID + "";
   }

}
