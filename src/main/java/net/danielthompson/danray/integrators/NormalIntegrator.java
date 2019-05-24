package net.danielthompson.danray.integrators;

import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Sample;

public class NormalIntegrator extends AbstractIntegrator {
   public NormalIntegrator(AbstractScene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   @Override
   public Sample GetSample(Ray ray, int depth, int x, int y) {
      Intersection intersection = scene.getNearestShape(ray, x, y);

      Sample sample = new Sample(x, y);
      sample.SpectralPowerDistribution = new SpectralPowerDistribution();

      if (intersection != null && intersection.hits) {
         sample.SpectralPowerDistribution.R = intersection.normal.x;
         sample.SpectralPowerDistribution.G = intersection.normal.y;
         sample.SpectralPowerDistribution.B = intersection.normal.z;
      }

      return sample;
   }
}
