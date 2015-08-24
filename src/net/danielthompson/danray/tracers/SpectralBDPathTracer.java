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
public class SpectralBDPathTracer extends SpectralTracer {

   public SpectralBDPathTracer(Scene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   private final double factor = 1.0;
   private final double iterations = 1.0;
   private final double adjustment = factor / iterations;

   public SpectralPowerDistribution GetSPDForRay(Ray ray, int depth) {

      SpectralPowerDistribution directSPD = new SpectralPowerDistribution();

      IntersectionState closestStateToRay = _scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null) {
         return directSPD;
      }

      if (closestStateToRay.Drawable instanceof SpectralRadiatable) {

         return ((SpectralRadiatable) closestStateToRay.Drawable).getSpectralPowerDistribution();
      }

      /// GENERATE LIGHT PATHS ///

      double lightPathWeight = 0;

      for (SpectralRadiatable radiatable : _scene.SpectralRadiatables) {
         Point intersectionPoint = closestStateToRay.IntersectionPoint;

         for (int i = 0; i < iterations; i++) {

            Point radiatableLocation = radiatable.getRandomPointOnSideOf(intersectionPoint);

            Ray lightRayFromCurrentRadiatableToClosestDrawable = intersectionPoint.CreateVectorFrom(radiatableLocation);

            // move the origin slightly outwards, in case we self-intersect
            Point origin = lightRayFromCurrentRadiatableToClosestDrawable.Origin;
            Vector direction = lightRayFromCurrentRadiatableToClosestDrawable.Direction;
            Vector offset = Vector.Scale(direction, -.0000001);

            IntersectionState potentialOccluder = _scene.GetClosestDrawableToRay(lightRayFromCurrentRadiatableToClosestDrawable);

            boolean noOccluder = (potentialOccluder == null);
            boolean targetIntersection = (potentialOccluder.Drawable.equals(closestStateToRay.Drawable) && Constants.WithinDelta(potentialOccluder.IntersectionPoint, closestStateToRay.IntersectionPoint));
            boolean shadowRayHitLight = potentialOccluder.Drawable.equals(radiatable);

            if (shadowRayHitLight) {
               potentialOccluder = _scene.GetClosestDrawableToRayBeyond(lightRayFromCurrentRadiatableToClosestDrawable, potentialOccluder.TMin);
            }

            noOccluder = (potentialOccluder == null);
            targetIntersection = (potentialOccluder.Drawable.equals(closestStateToRay.Drawable) && Constants.WithinDelta(potentialOccluder.IntersectionPoint, closestStateToRay.IntersectionPoint));
            shadowRayHitLight = potentialOccluder.Drawable.equals(radiatable);

            if (shadowRayHitLight) {
               // shit.. now what?
               ;
            }

            if (noOccluder || targetIntersection) {
               IntersectionState state = closestStateToRay.Drawable.GetHitInfo(lightRayFromCurrentRadiatableToClosestDrawable);
               if (state.Hits) {
                  // figure out how much light is shining by sampling the light
                  double pdf = radiatable.getPDF(intersectionPoint, direction);

                  double pdfPercentage = (4 * Math.PI) / pdf;
                  lightPathWeight = pdfPercentage;
                  SpectralPowerDistribution currentIncomingSPD = radiatable.getSpectralPowerDistribution();
                  currentIncomingSPD = SpectralPowerDistribution.scale(currentIncomingSPD, pdfPercentage);
                  directSPD.add(currentIncomingSPD);

               }
            }
            else {
               lightPathWeight = 0;
            }
         }
      }

      // directSPD now has contribution from direct lighting

      /// GENERATE EYE PATHS ///

      Drawable closestDrawable = closestStateToRay.Drawable;
      Material objectMaterial = closestDrawable.GetMaterial();

      Normal intersectionNormal = closestStateToRay.Normal;
      Vector incomingDirection = ray.Direction;

      Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection);
      double scalePercentage = objectMaterial.BRDF.f(incomingDirection, intersectionNormal, outgoingDirection);

      Ray bounceRay = new Ray(closestStateToRay.IntersectionPoint, outgoingDirection);

      closestStateToRay = _scene.GetClosestDrawableToRay(bounceRay);

      SpectralPowerDistribution indirectSPD = new SpectralPowerDistribution();

      if (closestStateToRay == null) {
         // indirectSPD should be blank
      }

      else if (closestStateToRay.Drawable instanceof SpectralRadiatable) {

         indirectSPD = ((SpectralRadiatable) closestStateToRay.Drawable).getSpectralPowerDistribution();
      }

      indirectSPD = SpectralPowerDistribution.scale(indirectSPD, scalePercentage);

      //indirectSPD = new SpectralPowerDistribution();

      /// COMBINE ////



      //SpectralPowerDistribution totalIncoming = SpectralPowerDistribution.add(directSPD, indirectSPD);
      SpectralPowerDistribution totalIncoming = SpectralPowerDistribution.lerp(directSPD, (float)(lightPathWeight), indirectSPD, (float)(1.0f -  lightPathWeight));

      SpectralReflectanceCurve curve = objectMaterial.SpectralReflectanceCurve;
      SpectralPowerDistribution reflectedSPD = totalIncoming.reflectOff(curve);
      return reflectedSPD;
   }

   //double float BalanceHeuristic(double )
}