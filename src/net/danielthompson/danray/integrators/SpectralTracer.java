package net.danielthompson.danray.integrators;

import net.danielthompson.danray.lights.SpectralRadiatable;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shading.SpectralReflectanceCurve;
import net.danielthompson.danray.shapes.Shape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.utility.GeometryCalculations;


/**
 * Created by daniel on 5/5/15.
 */
public class SpectralTracer extends AbstractIntegrator {

   private final double factor = 1.0;
   private final double iterations = 1.0;
   private final double adjustment = factor / iterations;

   public SpectralTracer(Scene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   public SpectralPowerDistribution GetSPDForRay(Ray ray, int depth) {

      SpectralPowerDistribution directSPD = new SpectralPowerDistribution();

      IntersectionState closestStateToRay = scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null || closestStateToRay.Shape == null) {
         return directSPD;
      }

      if (closestStateToRay.Shape instanceof SpectralRadiatable) {

         return ((SpectralRadiatable) closestStateToRay.Shape).getSpectralPowerDistribution();
      }

      Shape closestShape = closestStateToRay.Shape;

      Material objectMaterial = closestShape.GetMaterial();

      for (SpectralRadiatable radiatable : scene.SpectralRadiatables) {
         Point intersectionPoint = closestStateToRay.IntersectionPoint;

         for (int i = 0; i < iterations; i++) {

            Point radiatableLocation = radiatable.getRandomPointOnSurface();

            Ray lightRayFromCurrentRadiatableToClosestDrawable = intersectionPoint.CreateVectorFrom(radiatableLocation);

            // move the origin slightly outwards, in case we self-intersect
            Point origin = lightRayFromCurrentRadiatableToClosestDrawable.Origin;
            Vector direction = lightRayFromCurrentRadiatableToClosestDrawable.Direction;
            Vector offset = Vector.Scale(direction, .000001);
            origin.Plus(offset);
            IntersectionState potentialOccluder = scene.GetClosestDrawableToRay(lightRayFromCurrentRadiatableToClosestDrawable);

            boolean noOccluder = (potentialOccluder == null);

            boolean occluderIsDrawable = (
                  potentialOccluder.Shape.equals(closestStateToRay.Shape)
                        && Constants.WithinEpsilon(potentialOccluder.IntersectionPoint, closestStateToRay.IntersectionPoint)
            );

            boolean occluderIsLight = potentialOccluder.Shape.equals(radiatable);

            if (noOccluder || occluderIsDrawable || occluderIsLight) {

               double oneOverDistanceFromLightSource = 1 / Math.sqrt(radiatableLocation.SquaredDistanceBetween(closestStateToRay.IntersectionPoint));
               oneOverDistanceFromLightSource *= oneOverDistanceFromLightSource;

               IntersectionState state = closestStateToRay.Shape.GetHitInfo(lightRayFromCurrentRadiatableToClosestDrawable);
               if (state.Hits) {
                  double angleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(lightRayFromCurrentRadiatableToClosestDrawable, closestStateToRay);
                  if (angleOfIncidencePercentage >= 0 && angleOfIncidencePercentage <= 100) {
                     SpectralPowerDistribution currentIncomingSPD = radiatable.getSpectralPowerDistribution();
                     double scaleFactor = adjustment * angleOfIncidencePercentage * oneOverDistanceFromLightSource;
                     currentIncomingSPD = SpectralPowerDistribution.scale(currentIncomingSPD, scaleFactor);

                     directSPD.add(currentIncomingSPD);
                  }
               }
            }
         }
      }

      // compute the interaction of the incoming SPD with the object's SRC

      SpectralReflectanceCurve curve = objectMaterial.SpectralReflectanceCurve;

      // recursive case
      if (depth < maxDepth && objectMaterial._reflectivity > 0) {
         SpectralPowerDistribution reflectedSPD = null;
         Ray reflectedRay = GeometryCalculations.GetReflectedRay(closestStateToRay.IntersectionPoint, closestStateToRay.Normal, ray);
         reflectedSPD = GetSPDForRay(reflectedRay, depth + 1/*, oldIndexOfRefraction*/);

         double reflectionFactor = Math.pow(objectMaterial._reflectivity, 7);

         reflectedSPD = SpectralPowerDistribution.scale(reflectedSPD, reflectionFactor);
         directSPD.add(reflectedSPD);
      }

      // base case

      SpectralPowerDistribution objectSPD = directSPD.reflectOff(curve);
      return objectSPD;

      /*

      ColorWithStatistics refractedColor = null;

      if (objectMaterial.getTransparency() > 0) {

         Ray refractedRay = GeometryCalculations.GetRefractedRay(closestStateToRay, closestStateToRay.Normal, ray, oldIndexOfRefraction);
         refractedColor = GetSample(refractedRay, depth, closestStateToRay.Drawable.GetMaterial().getIndexOfRefraction());
         colorWithStatistics.Statistics.Add(refractedColor.Statistics);
      }
      */

      //float transparency = (float)objectMaterial.getTransparency();
      //Color[] colors = new Color[] {calculatedColor, reflectedColor == null ? null : reflectedColor.Color, refractedColor == null ? null : refractedColor.Color };
      //float[] weights = new float[] { (float)objectMaterial.getDiffuse(), reflectivity, transparency};
      //SpectralPowerDistribution blended = SpectralBlender.BlendWeighted(objectSPD, 1 - reflectivity, reflectedSPD, reflectivity);

      //return blended;
      /*
      Color blended;
      if (refractedColor == null || refractedColor == Color.magenta) {
         blended = Blender.BlendRGB(calculatedColor, reflectedColor, reflectivity);
      }
      else {
         blended = Blender.BlendRGB(calculatedColor, reflectedColor, reflectivity, refractedColor, transparency);
      }
      return blended;*/
      //return objectSPD;




      //return new ColorWithStatistics(Color.magenta, null); */
      

   }

}
