package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Vector;

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
         this._material = new Material();

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
