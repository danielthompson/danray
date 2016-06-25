package net.danielthompson.danray.integrators;

import net.danielthompson.danray.structures.Sample;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.scenes.AbstractScene;

/**
 * Created by daniel on 9/12/15.
 */
public abstract class AbstractIntegrator {
   AbstractScene scene;
   int maxDepth;

   protected AbstractIntegrator(AbstractScene scene, int maxDepth) {
      this.scene = scene;
      this.maxDepth = maxDepth;
   }

   public Sample GetSample(Ray ray, int depth) {
      return null;
   }
}
