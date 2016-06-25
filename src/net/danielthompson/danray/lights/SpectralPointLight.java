package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.fullspectrum.FullSpectralPowerDistribution;
import net.danielthompson.danray.structures.Point;

/**
 * DanRay
 * User: dthompson
 * Date: 6/30/13
 * Time: 9:30
 */
public class SpectralPointLight extends PointLight implements SpectralRadiatable {
   private FullSpectralPowerDistribution _spd;

   public SpectralPointLight(Point location, double lumens, FullSpectralPowerDistribution spd) {
      super(location, lumens);
      _spd = spd;
   }

   @Override
   public FullSpectralPowerDistribution getSpectralPowerDistribution() {
      return _spd;
   }

   public String toString() {
      return ID + "";
   }

   //public SpectralPowerDistribution SampleLight(Point pointOnSurface, Vector directionFromLightToPoint)
}