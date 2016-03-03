package net.danielthompson.danray.tracers;

import net.danielthompson.danray.lights.Radiatable;
import net.danielthompson.danray.shading.Blender;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.Drawable;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.states.IntersectionState;

import java.awt.*;

import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.utility.GeometryCalculations;

/**
 * User: daniel
 * Date: 7/2/13
 * Time: 15:26
 */
public class Tracer extends BaseTracer {

   private final int _airIndexOfRefraction = 1;

   private final double factor = 140.0;
   private final double iterations = 1.0;
   private final double adjustment = factor / iterations;

   public Tracer(Scene scene, int maxDepth) {
      super(scene, maxDepth);
   }


   public ColorWithStatistics GetColorForRay(Ray ray, int depth) {
      return GetColorForRay(ray, depth, _airIndexOfRefraction);
   }

   public ColorWithStatistics GetColorForRay(Ray ray, int depth, double oldIndexOfRefraction) {

      ColorWithStatistics colorWithStatistics = new ColorWithStatistics();

      double brightness = 0;

      IntersectionState closestStateToRay = scene.GetClosestDrawableToRay(ray);

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

         colorWithStatistics.Color = closestStateToRay.Drawable.GetMaterial().Color;
         return colorWithStatistics;
      }

      Drawable closestDrawable = closestStateToRay.Drawable;

      if (closestDrawable == null) {
         ;
      }

      Material objectMaterial = closestDrawable.GetMaterial();

      for (Radiatable radiatable : scene.Radiatables) {
         Point intersectionPoint = closestStateToRay.IntersectionPoint;

         for (int i = 0; i < iterations; i++) {

            Point radiatableLocation = radiatable.getRandomPointOnSurface();

            Ray lightRayFromCurrentRadiatableToClosestDrawable = intersectionPoint.CreateVectorFrom(radiatableLocation);
            IntersectionState potentialOccluder = scene.GetClosestDrawableToRay(lightRayFromCurrentRadiatableToClosestDrawable);

            if (potentialOccluder == null || potentialOccluder.Drawable.equals(closestStateToRay.Drawable) || potentialOccluder.Drawable.equals(radiatable)) {
               double oneOverDistanceFromLightSource = 1 / Math.sqrt(radiatableLocation.SquaredDistanceBetween(closestStateToRay.IntersectionPoint));
               oneOverDistanceFromLightSource *= oneOverDistanceFromLightSource;

               IntersectionState state = closestStateToRay.Drawable.GetHitInfo(lightRayFromCurrentRadiatableToClosestDrawable);
               if (state.Hits) {
                  double angleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(lightRayFromCurrentRadiatableToClosestDrawable, closestStateToRay);
                  if (angleOfIncidencePercentage >= 0 && angleOfIncidencePercentage <= 100) {
                     brightness += adjustment * radiatable.getPower() * (angleOfIncidencePercentage) * oneOverDistanceFromLightSource;
                  }
               }

            }
         }
      }

      float[] hsbColor = Color.RGBtoHSB(objectMaterial.Color.getRed(), objectMaterial.Color.getGreen(), objectMaterial.Color.getBlue(), null);

      hsbColor[2] = (float)brightness;

      if (hsbColor[2] >= 1.0f) {
         hsbColor[2] = 1.0f;
      }

      Color calculatedColor = Color.getHSBColor(hsbColor[0], hsbColor[1], hsbColor[2]);

      // base case
      if (depth >= maxDepth) {
         colorWithStatistics.Color = calculatedColor;
         return colorWithStatistics;
      }
      // recursive case
      else {
         depth++;
         // reflected color

         ColorWithStatistics reflectedColor = null;

         double reflectedWeight = 0.0;

         if (objectMaterial.BRDF != null) {
            Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(closestStateToRay.Normal, ray.Direction);

            Point offsetIntersection = Point.Plus(closestStateToRay.IntersectionPoint, Vector.Scale(outgoingDirection, Constants.Epsilon * 1000));

            Ray reflectedRay = new Ray(offsetIntersection, outgoingDirection);

            reflectedColor = GetColorForRay(reflectedRay, depth, oldIndexOfRefraction);
            colorWithStatistics.Statistics.Add(reflectedColor.Statistics);

            Vector reversedIncoming = Vector.Scale(ray.Direction, -1);

            double angleIncoming = GeometryCalculations.angleBetween(reversedIncoming, closestStateToRay.Normal);
            double angleOutgoing = GeometryCalculations.angleBetween(outgoingDirection, closestStateToRay.Normal);

            reflectedWeight = objectMaterial.BRDF.f(angleIncoming, angleOutgoing);
         }

         ColorWithStatistics refractedColor = null;
         /*
         else if (objectMaterial.getReflectivity() > 0) {
            Ray reflectedRay = GeometryCalculations.GetReflectedRay(closestStateToRay.IntersectionPoint, closestStateToRay.Normal, ray);

            reflectedColor = GetColorForRay(reflectedRay, depth, oldIndexOfRefraction);
            colorWithStatistics.Statistics.Add(reflectedColor.Statistics);

            // refracted color

            if (reflectedColor.Color == Color.magenta) {
               colorWithStatistics.Color = calculatedColor;
               return colorWithStatistics;
            }
         }

         if (objectMaterial.getTransparency() > 0) {

            Ray refractedRay = GeometryCalculations.GetRefractedRay(closestStateToRay, closestStateToRay.Normal, ray, oldIndexOfRefraction);
            refractedColor = GetColorForRay(refractedRay, depth, closestStateToRay.Drawable.GetMaterial().getIndexOfRefraction());
            colorWithStatistics.Statistics.Add(refractedColor.Statistics);
         }
*/
         float transparency = (float)objectMaterial._transparency;
         Color[] colors = new Color[] {calculatedColor, reflectedColor == null ? null : reflectedColor.Color, refractedColor == null ? null : refractedColor.Color };
         float[] weights = new float[] { (float)( 1- objectMaterial._specular), (float)reflectedWeight, transparency};
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
