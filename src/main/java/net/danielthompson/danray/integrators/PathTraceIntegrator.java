package net.danielthompson.danray.integrators;

import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;


/**
 * Created by daniel on 5/5/15.
 */
public class PathTraceIntegrator extends AbstractIntegrator {

   public PathTraceIntegrator(AbstractScene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   public Sample GetSample(Ray ray, int depth, int x, int y) {

      Sample sample = new Sample();
      Intersection intersection = scene.getNearestShape(ray, x, y);

      if (intersection == null || !intersection.Hits) {

         if (intersection != null) {
            sample.KDHeatCount = intersection.KDHeatCount;
         }
         sample.SpectralPowerDistribution = scene.getSkyBoxSPD(ray.Direction);
         return sample;
      }

      intersection.x = x;
      intersection.y = y;

      if (intersection.Shape instanceof AbstractLight) {
         sample.SpectralPowerDistribution = ((AbstractLight) intersection.Shape).SpectralPowerDistribution;
         return sample;
      }

      // base case
      if (depth >= maxDepth) {
         sample.SpectralPowerDistribution = new SpectralPowerDistribution();
         return sample;
      }
      else {

         AbstractShape closestShape = intersection.Shape;
//         if (closestShape == null) {
//            sample.SpectralPowerDistribution = new SpectralPowerDistribution();
//            return sample;
//         }
         Material objectMaterial = closestShape.Material;

         Normal intersectionNormal = intersection.Normal;
         Vector incomingDirection = ray.Direction;

         Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersection, incomingDirection);
         float scalePercentage = objectMaterial.BRDF.f(incomingDirection, intersectionNormal, outgoingDirection);

         Ray bounceRay = new Ray(intersection.Location, outgoingDirection);

         bounceRay.OffsetOrigin(intersectionNormal);
         //bounceRay.OffsetOriginForward(Constants.DoubleEpsilon);

         Sample incomingSample = GetSample(bounceRay, depth + 1, x, y);

         SpectralPowerDistribution incomingSPD = incomingSample.SpectralPowerDistribution;

         incomingSPD = SpectralPowerDistribution.scale(incomingSPD, scalePercentage);

         // compute the interaction of the incoming SPD with the object's SRC

         ReflectanceSpectrum reflectanceSpectrum = objectMaterial.ReflectanceSpectrum;

         SpectralPowerDistribution reflectedSPD = incomingSPD.reflectOff(reflectanceSpectrum);

         sample.SpectralPowerDistribution = reflectedSPD;

         return sample;
      }

   }

}
