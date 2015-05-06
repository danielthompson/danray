package net.danielthompson.danray;

import net.danielthompson.danray.lights.Radiatable;
import net.danielthompson.danray.lights.SpectralRadiatable;
import net.danielthompson.danray.shading.Blender;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.Drawable;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Scene;

import java.awt.*;


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

      double brightness = 0;

      IntersectionState closestStateToRay = _scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null) {
         if (depth == 1) {
            return sampleSPD;
         }
         else {
            // fix?
            return sampleSPD;
         }
      }
/*
      if (closestStateToRay.Drawable instanceof Radiatable) {

         colorWithStatistics.Color = closestStateToRay.Drawable.GetMaterial().getColor();
         return colorWithStatistics;
      }
*/
      Drawable closestDrawable = closestStateToRay.Drawable;

      if (closestDrawable == null) {
         ;
      }

      Material objectMaterial = closestDrawable.GetMaterial();

      SpectralPowerDistribution incomingSpectralPowerDistribution = new SpectralPowerDistribution();

      for (SpectralRadiatable radiatable : _scene.SpectralRadiatables) {
         Point intersectionPoint = closestStateToRay.IntersectionPoint;

         for (int i = 0; i < iterations; i++) {

            Point radiatableLocation = radiatable.getRandomPointOnSurface();

            Ray lightRayFromCurrentRadiatableToClosestDrawable = intersectionPoint.CreateVectorFrom(radiatableLocation);
            IntersectionState potentialOccluder = _scene.GetClosestDrawableToRay(lightRayFromCurrentRadiatableToClosestDrawable);

            if (potentialOccluder == null || potentialOccluder.Drawable.equals(closestStateToRay.Drawable) || potentialOccluder.Drawable.equals(radiatable)) {

               //double oneOverDistanceFromLightSource = Tracer.FastInverseSQRT(radiatableLocation.SquaredDistanceBetween(closestStateToRay.IntersectionPoint));
               double oneOverDistanceFromLightSource = 1 / Math.sqrt(radiatableLocation.SquaredDistanceBetween(closestStateToRay.IntersectionPoint));
               oneOverDistanceFromLightSource *= oneOverDistanceFromLightSource;

               IntersectionState state = closestStateToRay.Drawable.GetHitInfo(lightRayFromCurrentRadiatableToClosestDrawable);
               if (state.Hits) {
                  double angleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(lightRayFromCurrentRadiatableToClosestDrawable, closestStateToRay);
                  if (angleOfIncidencePercentage >= 0 && angleOfIncidencePercentage <= 100) {
                     SpectralPowerDistribution currentIncomingSPD = radiatable.getSpectralPowerDistribution();
                     double scaleFactor = adjustment * angleOfIncidencePercentage * oneOverDistanceFromLightSource;
                     currentIncomingSPD.scale(scaleFactor);

                     incomingSpectralPowerDistribution.add(currentIncomingSPD);

                     //brightness += adjustment * radiatable.getPower() * (angleOfIncidencePercentage) * oneOverDistanceFromLightSource;
                  }
               }
            }
         }
      }

      // base case
      if (depth >= _maxDepth) {
        ;

      }
      return incomingSpectralPowerDistribution;
      // recursive case
      /* else {

         depth++;
         // reflected color

         ColorWithStatistics reflectedColor = null;

         if (objectMaterial.getReflectivity() > 0) {
            //Vector reflectedRay = GetReflectedRayPerturbed(closestStateToRay.IntersectionPoint, closestStateToRay.Normal, ray, objectMaterial.getDiffuse());
            Ray reflectedRay = GeometryCalculations.GetReflectedRay(closestStateToRay.IntersectionPoint, closestStateToRay.Normal, ray);

            reflectedColor = GetColorForRay(reflectedRay, depth, oldIndexOfRefraction);
            colorWithStatistics.Statistics.Add(reflectedColor.Statistics);

            // refracted color

            if (reflectedColor.Color == Color.magenta) {
               colorWithStatistics.Color = calculatedColor;
               return colorWithStatistics;
            }
         }



         ColorWithStatistics refractedColor = null;

         if (objectMaterial.getTransparency() > 0) {

            Ray refractedRay = GeometryCalculations.GetRefractedRay(closestStateToRay, closestStateToRay.Normal, ray, oldIndexOfRefraction);
            refractedColor = GetColorForRay(refractedRay, depth, closestStateToRay.Drawable.GetMaterial().getIndexOfRefraction());
            colorWithStatistics.Statistics.Add(refractedColor.Statistics);
         }

         float reflectivity = (float)objectMaterial.getReflectivity();
         float transparency = (float)objectMaterial.getTransparency();
         Color[] colors = new Color[] {calculatedColor, reflectedColor == null ? null : reflectedColor.Color, refractedColor == null ? null : refractedColor.Color };
         float[] weights = new float[] { (float)objectMaterial.getDiffuse(), reflectivity, transparency};
         Color blended = Blender.BlendRGB(colors, weights);
         colorWithStatistics.Color = blended;
         return colorWithStatistics;
         /*
         Color blended;
         if (refractedColor == null || refractedColor == Color.magenta) {
            blended = Blender.BlendRGB(calculatedColor, reflectedColor, reflectivity);
         }
         else {
            blended = Blender.BlendRGB(calculatedColor, reflectedColor, reflectivity, refractedColor, transparency);
         }
         return blended;*/


/*
      //}
      //return new ColorWithStatistics(Color.magenta, null); */
      

   }

}
