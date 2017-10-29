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

      int foo = 0;

      Sample sample = new Sample();
      sample.SpectralPowerDistribution = new SpectralPowerDistribution();

      Ray[] rays = new Ray[maxDepth];
      float[] fs = new float[maxDepth];
      SpectralPowerDistribution[] spds = new SpectralPowerDistribution[maxDepth + 1];
      ReflectanceSpectrum[] refls = new ReflectanceSpectrum[maxDepth];
      IntersectionState[] states = new IntersectionState[maxDepth];

      spds[0] = new SpectralPowerDistribution();
      rays[0] = ray;

      int bounces = 0;

      if (x == 340 && y == 357) {
         foo++;
         foo--;
      }


      for (bounces = 0; bounces < maxDepth; bounces++) {

         IntersectionState closestStateToRay = scene.getNearestShape(rays[bounces], x, y);
         states[bounces] = closestStateToRay;

         if (closestStateToRay == null || !closestStateToRay.Hits) {

            spds[bounces].add(scene.getSkyBoxSPD(rays[bounces].Direction));
            break;
         }

         if (closestStateToRay.Shape instanceof AbstractLight) {
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
            refls[bounces] = objectMaterial.ReflectanceSpectrum;

            rays[bounces + 1] = new Ray(closestStateToRay.IntersectionPoint, outgoingDirection);
            rays[bounces + 1].OffsetOriginForward(Constants.HalfEpsilon);
            spds[bounces + 1] = new SpectralPowerDistribution();

         }
      }



      if (bounces == 0) {
         sample.SpectralPowerDistribution = spds[0];
      }

      else {

         for (int i = bounces - 1; i >= 0; i--) {
//            if (bounces > 0) {
//               foo++;
//               foo--;
//            }
//
//            if (i >= spds.length) {
//               foo++;
//               foo--;
//            }
//
            if (x == 340 && y == 357) {
               foo++;
               foo--;
            }

            SpectralPowerDistribution spd = spds[i + 1];

            ReflectanceSpectrum refl = null;
            float f = 0.0f;

            f = fs[i];
            refl = refls[i];

            if (refl == null) {
               sample.SpectralPowerDistribution = spd;
               if (spd == null)
                  sample.SpectralPowerDistribution = new SpectralPowerDistribution();
               break;
            }

            if (spd == null) {
               foo++;
               foo--;
            } else {
               spd = SpectralPowerDistribution.scale(spd, f);
            }

            // compute the interaction of the incoming SPD with the object's SRC

            SpectralPowerDistribution reflectedSPD = spd.reflectOff(refl);

            if (i > 0)
               spds[i] = reflectedSPD;
            else
               sample.SpectralPowerDistribution = reflectedSPD;
         }
         //sample.SpectralPowerDistribution = spds[0];
      }

      return sample;

   }

}
