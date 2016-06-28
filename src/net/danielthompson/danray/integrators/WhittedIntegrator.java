package net.danielthompson.danray.integrators;

import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.lights.Radiatable;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
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
public class WhittedIntegrator extends AbstractIntegrator {

   private final int _airIndexOfRefraction = 1;

   private final double factor = 140.0;
   private final double iterations = 1.0;
   private final double adjustment = factor / iterations;

   public WhittedIntegrator(AbstractScene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   @Override
   public Sample GetSample(Ray ray, int depth) {
      return GetSample(ray, depth, _airIndexOfRefraction);
   }

   public Sample GetSample(Ray ray, int depth, double oldIndexOfRefraction) {

      Sample sample = new Sample();

      double brightness = 0;

      IntersectionState closestStateToRay = scene.getNearestShape(ray);

      if (closestStateToRay == null || !closestStateToRay.Hits) {

         if (closestStateToRay != null)
            sample.KDHeatCount = closestStateToRay.KDHeatCount;
         if (depth == 1) {
            sample.SpectralPowerDistribution = new SpectralPowerDistribution(Color.black);
            return sample;
         }
         else {
            sample.SpectralPowerDistribution = new SpectralPowerDistribution(Color.magenta);
            return sample;
         }
      }

      sample.KDHeatCount = closestStateToRay.KDHeatCount;

      if (closestStateToRay.Shape instanceof Radiatable) {
         sample.SpectralPowerDistribution = ((Radiatable)closestStateToRay.Shape).getSPD();
         return sample;
      }

      Shape closestShape = closestStateToRay.Shape;

      Material objectMaterial = closestShape.GetMaterial();

      SpectralPowerDistribution directSPD = new SpectralPowerDistribution();

      // calculate direct light

      for (AbstractLight light : scene.Lights) {
         Point intersectionPoint = closestStateToRay.IntersectionPoint;

         for (int i = 0; i < iterations; i++) {

            Point lightLocation = light.getRandomPointOnSurface();

            Ray lightToNearestShape = intersectionPoint.CreateVectorFrom(lightLocation);

            if (closestStateToRay.Normal.Dot(lightToNearestShape.Direction) < 0) {

               IntersectionState potentialOccluder = scene.getNearestShape(lightToNearestShape);

               if (
                     potentialOccluder == null
                           || !potentialOccluder.Hits
                           || potentialOccluder.Shape.equals(closestStateToRay.Shape)
                           || potentialOccluder.Shape.equals(light)
                     ) {
                  double oneOverDistanceFromLightSource = 1 / Math.sqrt(lightLocation.SquaredDistanceBetween(closestStateToRay.IntersectionPoint));
                  oneOverDistanceFromLightSource *= oneOverDistanceFromLightSource;

                  IntersectionState state = closestStateToRay.Shape.getHitInfo(lightToNearestShape);
                  if (state.Hits) {
                     double angleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(lightToNearestShape, closestStateToRay);
                     if (angleOfIncidencePercentage >= 0 && angleOfIncidencePercentage <= 100) {
                        SpectralPowerDistribution scaledIncomingSPD = SpectralPowerDistribution.scale(light.SpectralPowerDistribution, (float)angleOfIncidencePercentage);
                        directSPD = SpectralPowerDistribution.add(directSPD, scaledIncomingSPD);

                        directSPD.scale((float) (adjustment * oneOverDistanceFromLightSource));
                        //brightness += adjustment * light.SpectralPowerDistribution.R * (angleOfIncidencePercentage) * oneOverDistanceFromLightSource;
                     }
                  }

               }
            }
         }
      }
//
//      int r = (int) (objectMaterial.ReflectanceSpectrum.R * 255);
//      int g = (int) (objectMaterial.ReflectanceSpectrum.G * 255);
//      int b = (int) (objectMaterial.ReflectanceSpectrum.B * 255);
//
//      float[] hsbColor = Color.RGBtoHSB(r, g, b, null);
//
//      hsbColor[2] = (float)brightness;
//
//      if (hsbColor[2] >= 1.0f) {
//         hsbColor[2] = 1.0f;
//      }
//
//      Color calculatedColor = Color.getHSBColor(hsbColor[0], hsbColor[1], hsbColor[2]);
//
//      SpectralPowerDistribution spd = new SpectralPowerDistribution(calculatedColor);

      directSPD = directSPD.reflectOff(objectMaterial.ReflectanceSpectrum);

      // base case
      if (depth >= maxDepth) {
         sample.SpectralPowerDistribution = directSPD;
         return sample;
      }
      // recursive case
      else {
         depth++;
         // reflected color

         Sample reflectedSample = null;

         double reflectedWeight = 0.0;

         if (objectMaterial.BRDF != null) {
            Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(closestStateToRay.Normal, ray.Direction);

            Point offsetIntersection = Point.Plus(closestStateToRay.IntersectionPoint, Vector.Scale(outgoingDirection, Constants.Epsilon * 1000));

            Ray reflectedRay = new Ray(offsetIntersection, outgoingDirection);

            reflectedSample = GetSample(reflectedRay, depth, oldIndexOfRefraction);

            Vector reversedIncoming = Vector.Scale(ray.Direction, -1);
/*
            double angleIncoming = GeometryCalculations.angleBetween(reversedIncoming, closestStateToRay.Normal);
            double angleOutgoing = GeometryCalculations.angleBetween(outgoingDirection, closestStateToRay.Normal);

            reflectedWeight = objectMaterial.BRDF.f(angleIncoming, angleOutgoing);
            reflectedWeight *= objectMaterial._transparency;
            */
         }

         Sample refractedSample = null;
         /*
         else if (objectMaterial.getReflectivity() > 0) {
            Ray reflectedRay = GeometryCalculations.GetReflectedRay(closestStateToRay.IntersectionPoint, closestStateToRay.Normal, ray);

            reflectedColor = GetSample(reflectedRay, depth, oldIndexOfRefraction);

            // refracted color

            if (reflectedColor.Color == Color.magenta) {
               colorWithStatistics.Color = calculatedColor;
               return colorWithStatistics;
            }
         }

         if (objectMaterial.getTransparency() > 0) {

            Ray refractedRay = GeometryCalculations.GetRefractedRay(closestStateToRay, closestStateToRay.Normal, ray, oldIndexOfRefraction);
            refractedSample = GetSample(refractedRay, depth, closestStateToRay.Drawable.GetMaterial().getIndexOfRefraction());
         }
*/
         float transparency = (float)objectMaterial._transparency;

         SpectralPowerDistribution blended = SpectralPowerDistribution.Lerp(reflectedSample.SpectralPowerDistribution, transparency, directSPD, 1.0f - transparency);

         Sample s = new Sample();
         s.SpectralPowerDistribution = blended;

         return s;
         /*
         Color blended;
         if (refractedSample == null || refractedSample == Color.magenta) {
            blended = Blender.BlendRGB(calculatedColor, reflectedColor, reflectivity);
         }
         else {
            blended = Blender.BlendRGB(calculatedColor, reflectedColor, reflectivity, refractedSample, transparency);
         }
         return blended;*/



      }
      //return new ColorWithStatistics(Color.magenta, null);
   }






}
