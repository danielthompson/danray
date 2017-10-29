package net.danielthompson.danray.integrators;

import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;

/**
 * Created by daniel on 5/5/15.
 */
public class IterativePathTraceIntegrator extends AbstractIntegrator {

   public IterativePathTraceIntegrator(AbstractScene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   public Sample GetSample(Ray ray, int depth, int x, int y) {

      Sample sample = new Sample();

      float[] fs = new float[maxDepth];
      SpectralPowerDistribution[] spds = new SpectralPowerDistribution[maxDepth + 1];
      ReflectanceSpectrum[] refls = new ReflectanceSpectrum[maxDepth];

      spds[0] = new SpectralPowerDistribution();

      int bounces;

      for (bounces = 0; bounces < maxDepth; bounces++) {

         IntersectionState closestStateToRay = scene.getNearestShape(ray, x, y);
         if (closestStateToRay == null || !closestStateToRay.Hits) {

            spds[bounces].add(scene.getSkyBoxSPD(ray.Direction));
            break;
         }

         if (closestStateToRay.Shape instanceof AbstractLight) {
            spds[bounces].add(((AbstractLight) closestStateToRay.Shape).SpectralPowerDistribution);
            break;
         }

         AbstractShape closestShape = closestStateToRay.Shape;

         if (bounces + 1 < maxDepth ) {
            Material objectMaterial = closestShape.Material;
            Normal intersectionNormal = closestStateToRay.Normal;
            Vector incomingDirection = ray.Direction;
            Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection);
            float scalePercentage = objectMaterial.BRDF.f(incomingDirection, intersectionNormal, outgoingDirection);

            fs[bounces] = scalePercentage;
            refls[bounces] = objectMaterial.ReflectanceSpectrum;

            ray = new Ray(closestStateToRay.IntersectionPoint, outgoingDirection);
            ray.OffsetOriginForward(Constants.HalfEpsilon);
            spds[bounces + 1] = new SpectralPowerDistribution();

         }
      }

      for (int i = bounces - 1; i >= 0; i--) {
         SpectralPowerDistribution spd = spds[i + 1];

         // fix this once direct lighting is added above
         if (refls[i] == null) {
            sample.SpectralPowerDistribution = spd;
            if (spd == null)
               sample.SpectralPowerDistribution = new SpectralPowerDistribution();
            break;
         }

         spd = SpectralPowerDistribution.scale(spd, fs[i]);

         SpectralPowerDistribution reflectedSPD = spd.reflectOff(refls[i]);

         spds[i] = reflectedSPD;
      }

      sample.SpectralPowerDistribution = spds[0];
      return sample;
   }

}
