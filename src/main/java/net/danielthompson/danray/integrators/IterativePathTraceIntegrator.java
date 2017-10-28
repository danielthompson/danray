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
      sample.SpectralPowerDistribution = new SpectralPowerDistribution();

      Ray[] rays = new Ray[maxDepth];
      float[] fs = new float[maxDepth];
      SpectralPowerDistribution[] spds = new SpectralPowerDistribution[maxDepth];
      ReflectanceSpectrum[] refls = new ReflectanceSpectrum[maxDepth];
      IntersectionState[] states = new IntersectionState[maxDepth];

      rays[0] = ray;

      int bounces;

      for (bounces = 0; bounces < depth; bounces++) {

         spds[bounces] = new SpectralPowerDistribution();

         IntersectionState closestStateToRay = scene.getNearestShape(rays[bounces], x, y);
         states[bounces] = closestStateToRay;

         if (closestStateToRay == null || !closestStateToRay.Hits) {

            if (bounces > 0) {
               bounces++;
               bounces--;
            }
            spds[bounces].add(scene.getSkyBoxSPD(ray.Direction));
            break;
         }

         if (closestStateToRay.Shape instanceof AbstractLight) {
            if (bounces > 0) {
               bounces++;
               bounces--;
            }
            spds[bounces].add(((AbstractLight) closestStateToRay.Shape).SpectralPowerDistribution);
            break;
         }

         AbstractShape closestShape = closestStateToRay.Shape;
         if (closestShape == null) {
            break;
         }

         if (bounces + 1 < maxDepth ) {
            Material objectMaterial = closestShape.Material;
            Normal intersectionNormal = closestStateToRay.Normal;
            Vector incomingDirection = ray.Direction;
            Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection);
            float scalePercentage = objectMaterial.BRDF.f(incomingDirection, intersectionNormal, outgoingDirection);
            fs[bounces] = scalePercentage;
            rays[bounces + 1] = new Ray(closestStateToRay.IntersectionPoint, outgoingDirection);
            rays[bounces + 1].OffsetOriginForward(Constants.HalfEpsilon);

            refls[bounces] = objectMaterial.ReflectanceSpectrum;
         }
      }

      //SpectralPowerDistribution[] spds2 = new SpectralPowerDistribution[maxDepth];

      if (bounces == 0) {
         sample.SpectralPowerDistribution = spds[0];
      }

      else {

         for (int i = bounces; i > 0; i++) {
            if (bounces > 0) {
               bounces++;
               bounces--;
            }

            SpectralPowerDistribution spd = spds[i];
            float f = fs[i];
            ReflectanceSpectrum refl = refls[i - 1];

            if (refl == null) {
               sample.SpectralPowerDistribution = spd;
               break;
            }

            if (spd == null) {
               bounces++;
               bounces--;
            } else {
               spd = SpectralPowerDistribution.scale(spd, f);
            }

            // compute the interaction of the incoming SPD with the object's SRC

            SpectralPowerDistribution reflectedSPD = spd.reflectOff(refl);

            if (i > 0)
               spds[i - 1] = reflectedSPD;
         }
         sample.SpectralPowerDistribution = spds[0];
      }

      return sample;

   }

}