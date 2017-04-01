package net.danielthompson.danray.integrators;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Sample;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.structures.Vector;

/**
 * Created by daniel on 9/12/15.
 */
public abstract class AbstractIntegrator {
   AbstractScene scene;
   int maxDepth;

   AbstractIntegrator(AbstractScene scene, int maxDepth) {
      this.scene = scene;
      this.maxDepth = maxDepth;
   }

   public Sample GetSample(Ray ray, int depth, int x, int y) {
      return null;
   }

   public SpectralPowerDistribution getSkyboxSPD(Vector v) {

      // intersect it with a 1x1x1 box and find intersection point




      return null;
   }
}
