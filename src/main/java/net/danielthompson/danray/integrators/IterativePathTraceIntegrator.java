package net.danielthompson.danray.integrators;

import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;

import java.util.ArrayList;


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

      for (int bounce = 0; bounce < depth; bounce++) {

         IntersectionState closestStateToRay = scene.getNearestShape(ray, x, y);

         if (closestStateToRay == null || !closestStateToRay.Hits) {

            if (closestStateToRay != null) {
               sample.KDHeatCount = closestStateToRay.KDHeatCount;
            }
            sample.SpectralPowerDistribution.add(scene.getSkyBoxSPD(ray.Direction));
            break;
         }

         if (closestStateToRay.Shape instanceof AbstractLight) {
            sample.SpectralPowerDistribution.add(((AbstractLight) closestStateToRay.Shape).SpectralPowerDistribution);
            break;
         }

         AbstractShape closestShape = closestStateToRay.Shape;
         if (closestShape == null) {
            break;
         }

         Material objectMaterial = closestShape.Material;
         Normal intersectionNormal = closestStateToRay.Normal;
         Vector incomingDirection = ray.Direction;
         Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection);
         float scalePercentage = objectMaterial.BRDF.f(incomingDirection, intersectionNormal, outgoingDirection);
         ray = new Ray(closestStateToRay.IntersectionPoint, outgoingDirection);
         ray.OffsetOriginForward(Constants.HalfEpsilon);

         //Sample incomingSample = GetSample(bounceRay, depth + 1, x, y);

         //SpectralPowerDistribution incomingSPD = incomingSample.SpectralPowerDistribution;

         //incomingSPD = SpectralPowerDistribution.scale(incomingSPD, scalePercentage);

         // compute the interaction of the incoming SPD with the object's SRC

         ReflectanceSpectrum reflectanceSpectrum = objectMaterial.ReflectanceSpectrum;

         SpectralPowerDistribution reflectedSPD = sample.SpectralPowerDistribution.reflectOff(reflectanceSpectrum);

         sample.SpectralPowerDistribution.add(reflectedSPD);

      }

      return sample;

   }

}
