package net.danielthompson.danray.samplers;

import net.danielthompson.danray.lights.SpectralRadiatable;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shading.SpectralReflectanceCurve;
import net.danielthompson.danray.shapes.Shape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;


/**
 * Created by daniel on 5/5/15.
 */
public class SpectralPathTracer extends BaseSampler {

   public SpectralPathTracer(Scene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   public SpectralPowerDistribution GetSPDForRay(Ray ray, int depth) {

      SpectralPowerDistribution directSPD = new SpectralPowerDistribution();

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
            return new SpectralPowerDistribution();
         }
         Material objectMaterial = closestShape.GetMaterial();

         Normal intersectionNormal = closestStateToRay.Normal;
         Vector incomingDirection = ray.Direction;

         Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection);
         double scalePercentage = objectMaterial.BRDF.f(incomingDirection, intersectionNormal, outgoingDirection);

         Ray bounceRay = new Ray(closestStateToRay.IntersectionPoint, outgoingDirection);

         //Ray indirectRay = GeometryCalculations.GetRandomRayInNormalHemisphere(closestStateToRay.IntersectionPoint, closestStateToRay.Normal);

         SpectralPowerDistribution incomingSPD = GetSPDForRay(bounceRay, depth + 1);

         incomingSPD = SpectralPowerDistribution.scale(incomingSPD, scalePercentage);

         // compute the interaction of the incoming SPD with the object's SRC

         SpectralReflectanceCurve curve = objectMaterial.SpectralReflectanceCurve;

         SpectralPowerDistribution reflectedSPD = incomingSPD.reflectOff(curve);

         //reflectedSPD.scale(.9);

         return reflectedSPD;
      }

   }

}
