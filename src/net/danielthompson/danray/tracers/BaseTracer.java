package net.danielthompson.danray.tracers;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.ColorWithStatistics;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Scene;

/**
 * Created by daniel on 9/12/15.
 */
public abstract class BaseTracer {
   Scene scene;
   int maxDepth;

   protected BaseTracer(Scene scene, int maxDepth) {
      this.scene = scene;
      this.maxDepth = maxDepth;
   }

   public SpectralPowerDistribution GetSPDForRay(Ray ray, int depth) {
      return null;
   }

   public ColorWithStatistics GetColorForRay(Ray ray, int depth) {
      return null;
   }
}
