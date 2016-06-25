package net.danielthompson.danray.integrators;

import net.danielthompson.danray.lights.SpectralRadiatable;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.fullspectrum.FullSpectralPowerDistribution;
import net.danielthompson.danray.shading.fullspectrum.FullSpectralReflectanceCurve;
import net.danielthompson.danray.shapes.Shape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;


/**
 * Created by daniel on 5/5/15.
 */
public class SpectralPathTracer extends AbstractIntegrator {

   public SpectralPathTracer(Scene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   public FullSpectralPowerDistribution GetSPDForRay(Ray ray, int depth) {

      FullSpectralPowerDistribution directSPD = new FullSpectralPowerDistribution();

      IntersectionState closestStateToRay = scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null) {
         return directSPD;
      }

      if (closestStateToRay.Shape instanceof SpectralRadiatable) {

         return ((SpectralRadiatable) closestStateToRay.Shape).getSpectralPowerDistribution();
      }

      // base case
      if (depth >= maxDepth) {
         return directSPD;
      }
      else {
         Shape closestShape = closestStateToRay.Shape;
         if (closestShape == null) {
            return new FullSpectralPowerDistribution();
         }
         Material objectMaterial = closestShape.GetMaterial();

         Normal intersectionNormal = closestStateToRay.Normal;
         Vector incomingDirection = ray.Direction;

         Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection);
         double scalePercentage = objectMaterial.BRDF.f(incomingDirection, intersectionNormal, outgoingDirection);

         Ray bounceRay = new Ray(closestStateToRay.IntersectionPoint, outgoingDirection);

         //Ray indirectRay = GeometryCalculations.GetRandomRayInNormalHemisphere(closestStateToRay.IntersectionPoint, closestStateToRay.Normal);

         FullSpectralPowerDistribution incomingSPD = GetSPDForRay(bounceRay, depth + 1);

         incomingSPD = FullSpectralPowerDistribution.scale(incomingSPD, scalePercentage);

         // compute the interaction of the incoming SPD with the object's SRC

         FullSpectralReflectanceCurve curve = objectMaterial.FullSpectralReflectanceCurve;

         FullSpectralPowerDistribution reflectedSPD = incomingSPD.reflectOff(curve);

         //reflectedSPD.scale(.9);

         return reflectedSPD;
      }

   }

}
