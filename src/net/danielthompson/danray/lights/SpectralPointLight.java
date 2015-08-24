package net.danielthompson.danray.lights;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;

/**
 * DanRay
 * User: dthompson
 * Date: 6/30/13
 * Time: 9:30
 */
public class SpectralPointLight extends PointLight implements SpectralRadiatable {
   private SpectralPowerDistribution _spd;

   public SpectralPointLight(Point location, double lumens, SpectralPowerDistribution spd) {
      super(location, lumens);
      _spd = spd;
   }

   @Override
   public SpectralPowerDistribution getSpectralPowerDistribution() {
      return _spd;
   }

   public String toString() {
      return ID + "";
   }

   //public SpectralPowerDistribution SampleLight(Point pointOnSurface, Vector directionFromLightToPoint)
}