package net.danielthompson.danray.integrators;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Sample;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Scene;

/**
 * Created by daniel on 9/12/15.
 */
public abstract class AbstractIntegrator {
   Scene scene;
   int maxDepth;

   protected AbstractIntegrator(Scene scene, int maxDepth) {
      this.scene = scene;
      this.maxDepth = maxDepth;
   }

   public SpectralPowerDistribution GetSPDForRay(Ray ray, int depth) {
      return null;
   }

   public Sample GetSample(Ray ray, int depth) {
      return null;
   }
}
