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
public class SpectralTracer {

   Scene _scene;
   int _maxDepth;

   private final double factor = 1.0;
   private final double iterations = 2.0;
   private final double adjustment = factor / iterations;

   public SpectralTracer(Scene scene, int maxDepth) {
      _scene = scene;
      _maxDepth = maxDepth;
   }

   public SpectralPowerDistribution GetSPDForRay(Ray ray, int depth) {

      SpectralPowerDistribution sampleSPD = new SpectralPowerDistribution();

      IntersectionState closestStateToRay = _scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null) {
         return sampleSPD;
      }

      if (closestStateToRay.Drawable instanceof SpectralRadiatable) {

         return ((SpectralRadiatable) closestStateToRay.Drawable).getSpectralPowerDistribution();
      }

      // base case
      if (depth >= _maxDepth) {
         return sampleSPD;
      }

      Drawable closestDrawable = closestStateToRay.Drawable;

      Material objectMaterial = closestDrawable.GetMaterial();



      for (SpectralRadiatable radiatable : _scene.SpectralRadiatables) {
         Point intersectionPoint = closestStateToRay.IntersectionPoint;

         for (int i = 0; i < iterations; i++) {

            Point radiatableLocation = radiatable.getRandomPointOnSurface();

            Ray lightRayFromCurrentRadiatableToClosestDrawable = intersectionPoint.CreateVectorFrom(radiatableLocation);

            // move the origin slightly outwards, in case we self-intersect
            Point origin = lightRayFromCurrentRadiatableToClosestDrawable.Origin;
            Vector direction = lightRayFromCurrentRadiatableToClosestDrawable.Direction;
            Vector offset = Vector.Scale(direction, -.0000001);
            origin.Plus(offset);
            //origin.Plus(lightRayFromCurrentRadiatableToClosestDrawable.Direction.);
            IntersectionState potentialOccluder = _scene.GetClosestDrawableToRay(lightRayFromCurrentRadiatableToClosestDrawable);

            if (potentialOccluder == null || (potentialOccluder.Drawable.equals(closestStateToRay.Drawable) && Constants.WithinDelta(potentialOccluder.IntersectionPoint, closestStateToRay.IntersectionPoint)) || potentialOccluder.Drawable.equals(radiatable)) {

               //double oneOverDistanceFromLightSource = Tracer.FastInverseSQRT(radiatableLocation.SquaredDistanceBetween(closestStateToRay.IntersectionPoint));
               double oneOverDistanceFromLightSource = 1 / Math.sqrt(radiatableLocation.SquaredDistanceBetween(closestStateToRay.IntersectionPoint));
               oneOverDistanceFromLightSource *= oneOverDistanceFromLightSource;

               IntersectionState state = closestStateToRay.Drawable.GetHitInfo(lightRayFromCurrentRadiatableToClosestDrawable);
               if (state.Hits) {
                  double angleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(lightRayFromCurrentRadiatableToClosestDrawable, closestStateToRay);
                  if (angleOfIncidencePercentage >= 0 && angleOfIncidencePercentage <= 100) {
                     SpectralPowerDistribution currentIncomingSPD = radiatable.getSpectralPowerDistribution();
                     double scaleFactor = adjustment * angleOfIncidencePercentage * oneOverDistanceFromLightSource;
                     currentIncomingSPD = SpectralPowerDistribution.scale(currentIncomingSPD, scaleFactor);

                     sampleSPD.add(currentIncomingSPD);

                     //brightness += adjustment * radiatable.getPower() * (angleOfIncidencePercentage) * oneOverDistanceFromLightSource;
                  }
               }
            }
         }
      }


      // compute the interaction of the incoming SPD with the object's SRC

      SpectralReflectanceCurve curve = objectMaterial.SpectralReflectanceCurve;

      SpectralPowerDistribution objectSPD; // = incomingSpectralPowerDistribution.reflectOff(curve);



      // recursive case

      depth++;
      SpectralPowerDistribution reflectedSPD = null;

      //double reflectivity = objectMaterial.getReflectivity();

      //if (reflectivity > 0) {
         Ray reflectedRay = GeometryCalculations.GetRandomRayInNormalHemisphere(closestStateToRay.IntersectionPoint, closestStateToRay.Normal);
      reflectedSPD = GetSPDForRay(reflectedRay, depth/*, oldIndexOfRefraction*/);
      reflectedSPD = SpectralPowerDistribution.scale(reflectedSPD, .75);
      reflectedSPD.add(sampleSPD);
         //incomingSpectralPowerDistribution.add(reflectedSPD);
         objectSPD = reflectedSPD.reflectOff(curve);
         return objectSPD;
      //}

      /*

      ColorWithStatistics refractedColor = null;

      if (objectMaterial.getTransparency() > 0) {

         Ray refractedRay = GeometryCalculations.GetRefractedRay(closestStateToRay, closestStateToRay.Normal, ray, oldIndexOfRefraction);
         refractedColor = GetColorForRay(refractedRay, depth, closestStateToRay.Drawable.GetMaterial().getIndexOfRefraction());
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
