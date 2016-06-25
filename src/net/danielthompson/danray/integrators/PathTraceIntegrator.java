package net.danielthompson.danray.integrators;

import net.danielthompson.danray.lights.Radiatable;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.Shape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;


/**
 * Created by daniel on 5/5/15.
 */
public class PathTraceIntegrator extends AbstractIntegrator {

   public PathTraceIntegrator(AbstractScene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   public Sample GetSample(Ray ray, int depth) {

      Sample sample = new Sample();
      IntersectionState closestStateToRay = scene.getNearestShape(ray);

      if (closestStateToRay == null) {
         sample.SpectralPowerDistribution = new SpectralPowerDistribution();
         return sample;
      }

      if (closestStateToRay.Shape instanceof Radiatable) {
         sample.SpectralPowerDistribution = ((Radiatable) closestStateToRay.Shape).getSPD();
         return sample;
      }

      // base case
      if (depth >= maxDepth) {
         sample.SpectralPowerDistribution = new SpectralPowerDistribution();
         return sample;
      }
      else {
         Shape closestShape = closestStateToRay.Shape;
         if (closestShape == null) {
            sample.SpectralPowerDistribution = new SpectralPowerDistribution();
            return sample;
         }
         Material objectMaterial = closestShape.GetMaterial();

         Normal intersectionNormal = closestStateToRay.Normal;
         Vector incomingDirection = ray.Direction;

         Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection);
         float scalePercentage = (float)objectMaterial.BRDF.f(incomingDirection, intersectionNormal, outgoingDirection);

         Ray bounceRay = new Ray(closestStateToRay.IntersectionPoint, outgoingDirection);

         //Ray indirectRay = GeometryCalculations.GetRandomRayInNormalHemisphere(closestStateToRay.IntersectionPoint, closestStateToRay.Normal);

         Sample incomingSample = GetSample(bounceRay, depth + 1);

         SpectralPowerDistribution incomingSPD = incomingSample.SpectralPowerDistribution;

         incomingSPD = SpectralPowerDistribution.scale(incomingSPD, scalePercentage);

         // compute the interaction of the incoming SPD with the object's SRC

         ReflectanceSpectrum reflectanceSpectrum = objectMaterial.ReflectanceSpectrum;

         SpectralPowerDistribution reflectedSPD = incomingSPD.reflectOff(reflectanceSpectrum);

         sample.SpectralPowerDistribution = reflectedSPD;

         //reflectedSPD.scale(.9);

         return sample;
      }

   }

}
