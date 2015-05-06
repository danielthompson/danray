package net.danielthompson.danray;

import net.danielthompson.danray.lights.Radiatable;
import net.danielthompson.danray.shading.Blender;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.Drawable;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.states.IntersectionState;

import java.awt.*;

import net.danielthompson.danray.structures.Point;

/**
 * User: daniel
 * Date: 7/2/13
 * Time: 15:26
 */
public class Tracer {

   Scene _scene;
   int _maxDepth;
   private final int _airIndexOfRefraction = 1;

   private final double factor = 140.0;
   private final double iterations = 2.0;
   private final double adjustment = factor / iterations;

   public Tracer(Scene scene, int maxDepth) {
      _scene = scene;
      _maxDepth = maxDepth;
   }


   public ColorWithStatistics GetColorForRay(Ray ray, int depth) {
      return GetColorForRay(ray, depth, _airIndexOfRefraction);
   }

   public ColorWithStatistics GetColorForRay(Ray ray, int depth, double oldIndexOfRefraction) {

      ColorWithStatistics colorWithStatistics = new ColorWithStatistics();

      double brightness = 0;

      IntersectionState closestStateToRay = _scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null) {
         if (depth == 1) {
            colorWithStatistics.Color = Color.black;
            return colorWithStatistics;
         }
         else {
            colorWithStatistics.Color = Color.magenta;
            return colorWithStatistics;
         }
      }

      colorWithStatistics.Statistics = closestStateToRay.Statistics;

      if (closestStateToRay.Drawable instanceof Radiatable) {

         colorWithStatistics.Color = closestStateToRay.Drawable.GetMaterial().getColor();
         return colorWithStatistics;
      }

      Drawable closestDrawable = closestStateToRay.Drawable;

      if (closestDrawable == null) {
         ;
      }

      Material objectMaterial = closestDrawable.GetMaterial();

      for (Radiatable radiatable : _scene._radiatables) {
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
                  // case 1
                  if (angleOfIncidencePercentage >= 0 && angleOfIncidencePercentage <= 100) {
                     brightness += adjustment * radiatable.getPower() * (angleOfIncidencePercentage) * oneOverDistanceFromLightSource;
                  }
               }

               /*
               for (Drawable drawable : _scene.getDrawables()) {

                  IntersectionState state = drawable.GetHitInfo(lightRayFromCurrentRadiatableToClosestDrawable);
                  if (state.Hits) {
                     if (drawable.equals(closestStateToRay.Drawable)) {
                        double angleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(lightRayFromCurrentRadiatableToClosestDrawable, closestStateToRay);
                        // case 1
                        if (angleOfIncidencePercentage >= 0 && angleOfIncidencePercentage <= 100) {
                           brightness += 140 * radiatable.getPower() * (angleOfIncidencePercentage) * oneOverDistanceFromLightSource;
                        }
                        // case 2
                        else {
                           // add no brightness
                        }
                     }
                     // case 3
                     else {
                        // add no brightness
                     }
                  }
               }
               */
            }
         }
      }

      float[] hsbColor = Color.RGBtoHSB(objectMaterial.getColor().getRed(), objectMaterial.getColor().getGreen(), objectMaterial.getColor().getBlue(), null);

      hsbColor[2] = (float)brightness;

      if (hsbColor[2] >= 1.0f) {
         hsbColor[2] = 1.0f;
      }

      Color calculatedColor = Color.getHSBColor(hsbColor[0], hsbColor[1], hsbColor[2]);

      // base case
      if (depth >= _maxDepth) {
         colorWithStatistics.Color = calculatedColor;
         return colorWithStatistics;
      }
      // recursive case
      else {
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



      }
      //return new ColorWithStatistics(Color.magenta, null);
   }






}
