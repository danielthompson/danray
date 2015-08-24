package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.SpectralPowerDistribution;

/**
 * Created by daniel on 5/5/15.
 */
public interface SpectralRadiatable extends Radiatable {
   SpectralPowerDistribution getSpectralPowerDistribution();


}
