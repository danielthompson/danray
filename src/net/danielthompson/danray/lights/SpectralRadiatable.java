package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.fullspectrum.FullSpectralPowerDistribution;

/**
 * Created by daniel on 5/5/15.
 */
public interface SpectralRadiatable extends Radiatable {
   FullSpectralPowerDistribution getSpectralPowerDistribution();


}
