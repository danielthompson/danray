package net.danielthompson.danray.integrators;

import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Sample;

public class DepthIntegrator extends AbstractIntegrator {
   public DepthIntegrator(AbstractScene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   @Override
   public Sample getSample(Ray ray, int depth, int x, int y) {
      Intersection intersection = scene.getNearestShape(ray, x, y);

      Sample sample = new Sample(x, y);
      sample.SpectralPowerDistribution = new SpectralPowerDistribution();

      float value = 0;

      if (intersection != null && intersection.hits) {

         value = 100.0f / intersection.t;

         sample.SpectralPowerDistribution.R = value;
         sample.SpectralPowerDistribution.G = value;
         sample.SpectralPowerDistribution.B = value;
      }

      return sample;
   }
}
