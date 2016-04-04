package net.danielthompson.danray.samplers;

import net.danielthompson.danray.lights.Radiatable;
import net.danielthompson.danray.shading.Blender;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.Shape;
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
public class WhittedSampler extends BaseSampler {

   private final int _airIndexOfRefraction = 1;

   private final double factor = 140.0;
   private final double iterations = 1.0;
   private final double adjustment = factor / iterations;

   public WhittedSampler(Scene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   @Override
   public Sample GetSample(Ray ray, int depth) {
      return GetColorForRay(ray, depth, _airIndexOfRefraction);
   }

   public Sample GetColorForRay(Ray ray, int depth, double oldIndexOfRefraction) {

      Sample sample = new Sample();

      double brightness = 0;

      IntersectionState closestStateToRay = scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null || !closestStateToRay.Hits) {

         if (closestStateToRay != null)
            sample.KDHeatCount = closestStateToRay.KDHeatCount;
         if (depth == 1) {
            sample.Color = Color.black;
            return sample;
         }
         else {
            sample.Color = Color.magenta;
            return sample;
         }
      }

      sample.Statistics = closestStateToRay.Statistics;
      sample.KDHeatCount = closestStateToRay.KDHeatCount;



      if (closestStateToRay.Shape instanceof Radiatable) {

         sample.Color = closestStateToRay.Shape.GetMaterial().Color;
         return sample;
      }

      Shape closestShape = closestStateToRay.Shape;

      if (closestShape == null) {
         ;
      }

      Material objectMaterial = closestShape.GetMaterial();

      for (Radiatable radiatable : scene.Radiatables) {
         Point intersectionPoint = closestStateToRay.IntersectionPoint;

         for (int i = 0; i < iterations; i++) {

            Point radiatableLocation = radiatable.getRandomPointOnSurface();

            Ray lightRayFromCurrentRadiatableToClosestDrawable = intersectionPoint.CreateVectorFrom(radiatableLocation);
            if (ray.Origin.X == 212.5 && ray.Origin.Y == 96.5 && ray.Origin.Z == 301)
               System.out.flush();
            IntersectionState potentialOccluder = scene.GetClosestDrawableToRay(lightRayFromCurrentRadiatableToClosestDrawable);

            if (potentialOccluder == null
                  || !potentialOccluder.Hits
                  || potentialOccluder.Shape.equals(closestStateToRay.Shape)
                  || potentialOccluder.Shape.equals(radiatable)) {
               double oneOverDistanceFromLightSource = 1 / Math.sqrt(radiatableLocation.SquaredDistanceBetween(closestStateToRay.IntersectionPoint));
               oneOverDistanceFromLightSource *= oneOverDistanceFromLightSource;

               IntersectionState state = closestStateToRay.Shape.GetHitInfo(lightRayFromCurrentRadiatableToClosestDrawable);
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
         sample.Color = calculatedColor;
         return sample;
      }
      // recursive case
      else {
         depth++;
         // reflected color

         Sample reflectedColor = null;

         double reflectedWeight = 0.0;

         if (objectMaterial.BRDF != null) {
            Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(closestStateToRay.Normal, ray.Direction);

            Point offsetIntersection = Point.Plus(closestStateToRay.IntersectionPoint, Vector.Scale(outgoingDirection, Constants.Epsilon * 1000));

            Ray reflectedRay = new Ray(offsetIntersection, outgoingDirection);

            reflectedColor = GetColorForRay(reflectedRay, depth, oldIndexOfRefraction);
            sample.Statistics.Add(reflectedColor.Statistics);

            Vector reversedIncoming = Vector.Scale(ray.Direction, -1);

            double angleIncoming = GeometryCalculations.angleBetween(reversedIncoming, closestStateToRay.Normal);
            double angleOutgoing = GeometryCalculations.angleBetween(outgoingDirection, closestStateToRay.Normal);

            reflectedWeight = objectMaterial.BRDF.f(angleIncoming, angleOutgoing);
            reflectedWeight *= objectMaterial._transparency;
         }

         Sample refractedColor = null;
         /*
         else if (objectMaterial.getReflectivity() > 0) {
            Ray reflectedRay = GeometryCalculations.GetReflectedRay(closestStateToRay.IntersectionPoint, closestStateToRay.Normal, ray);

            reflectedColor = GetSample(reflectedRay, depth, oldIndexOfRefraction);
            colorWithStatistics.Statistics.Add(reflectedColor.Statistics);

            // refracted color

            if (reflectedColor.Color == Color.magenta) {
               colorWithStatistics.Color = calculatedColor;
               return colorWithStatistics;
            }
         }

         if (objectMaterial.getTransparency() > 0) {

            Ray refractedRay = GeometryCalculations.GetRefractedRay(closestStateToRay, closestStateToRay.Normal, ray, oldIndexOfRefraction);
            refractedColor = GetSample(refractedRay, depth, closestStateToRay.Drawable.GetMaterial().getIndexOfRefraction());
            colorWithStatistics.Statistics.Add(refractedColor.Statistics);
         }
*/
         float transparency = (float)objectMaterial._transparency;
         Color[] colors = new Color[] {calculatedColor, reflectedColor == null ? null : reflectedColor.Color, refractedColor == null ? null : refractedColor.Color };
         float[] weights = new float[] { (float)( objectMaterial._intrinsic), (float)reflectedWeight, transparency};
         Color blended = Blender.BlendRGB(colors, weights);
         sample.Color = blended;
         return sample;
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
