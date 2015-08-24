package net.danielthompson.danray.tracers;

import net.danielthompson.danray.lights.SpectralRadiatable;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shading.SpectralReflectanceCurve;
import net.danielthompson.danray.shapes.Drawable;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;


/**
 * Created by daniel on 5/5/15.
 */
public class SpectralPathTracer extends SpectralTracer {

   //private final double factor = 1.0;
   //private final double iterations = 1.0;
   //private final double adjustment = factor / iterations;

   public SpectralPathTracer(Scene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   public SpectralPowerDistribution GetSPDForRay(Ray ray, int depth) {

      SpectralPowerDistribution directSPD = new SpectralPowerDistribution();

      IntersectionState closestStateToRay = _scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null) {
         return directSPD;
      }

      if (closestStateToRay.Drawable instanceof SpectralRadiatable) {

         return ((SpectralRadiatable) closestStateToRay.Drawable).getSpectralPowerDistribution();
      }

      // base case
      if (depth >= _maxDepth) {
         return directSPD;
      }
      else {
         Drawable closestDrawable = closestStateToRay.Drawable;
         Material objectMaterial = closestDrawable.GetMaterial();

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
