package net.danielthompson.danray.integrators;

import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.AbstractShape;
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

   public WhittedIntegrator(AbstractScene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   @Override
   public Sample GetSample(Ray ray, int depth) {
      return GetSample(ray, depth, _airIndexOfRefraction);
   }

   private Sample GetSample(Ray ray, int depth, float oldIndexOfRefraction) {

      Sample sample = new Sample();

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

      if (closestStateToRay.Shape instanceof AbstractLight) {
         sample.SpectralPowerDistribution = ((AbstractLight)closestStateToRay.Shape).SpectralPowerDistribution;
         return sample;
      }

      AbstractShape closestShape = closestStateToRay.Shape;

      Material objectMaterial = closestShape.Material;

      SpectralPowerDistribution directSPD = new SpectralPowerDistribution();

      // calculate direct light

      for (AbstractLight light : scene.Lights) {
         Point intersectionPoint = closestStateToRay.IntersectionPoint;

         Point lightLocation = light.getRandomPointOnSurface();

         Ray lightToNearestShape = intersectionPoint.CreateVectorFrom(lightLocation);

         float dot = closestStateToRay.Normal.Dot(lightToNearestShape.Direction);

         if (dot < 0) {

            IntersectionState potentialOccluder = scene.getNearestShape(lightToNearestShape);

            if (
                  potentialOccluder == null
                        || !potentialOccluder.Hits
                        || potentialOccluder.Shape.equals(closestStateToRay.Shape)
                        || potentialOccluder.Shape.equals(light)
                  ) {
               float oneOverDistanceFromLightSourceSquared = 1 / lightLocation.SquaredDistanceBetween(closestStateToRay.IntersectionPoint);

               IntersectionState state = closestStateToRay.Shape.getHitInfo(lightToNearestShape);
               if (state.Hits) {
                  float angleOfIncidencePercentage = GeometryCalculations.GetAngleOfIncidencePercentage(lightToNearestShape, closestStateToRay);
                  SpectralPowerDistribution scaledIncomingSPD = SpectralPowerDistribution.scale(light.SpectralPowerDistribution, (float)angleOfIncidencePercentage);
                  scaledIncomingSPD.scale((float) (oneOverDistanceFromLightSourceSquared));
                  directSPD = SpectralPowerDistribution.add(directSPD, scaledIncomingSPD);
               }
            }
         }
      }

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

         float reflectedWeight = 0.0f;

         if (objectMaterial.BRDF != null) {
            Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(closestStateToRay.Normal, ray.Direction);

            Point offsetIntersection = Point.Plus(closestStateToRay.IntersectionPoint, Vector.Scale(outgoingDirection, Constants.Epsilon * 1000));

            Ray reflectedRay = new Ray(offsetIntersection, outgoingDirection);

            reflectedSample = GetSample(reflectedRay, depth, oldIndexOfRefraction);

            Vector reversedIncoming = Vector.Scale(ray.Direction, -1);
/*
            float angleIncoming = GeometryCalculations.radiansBetween(reversedIncoming, closestStateToRay.Normal);
            float angleOutgoing = GeometryCalculations.radiansBetween(outgoingDirection, closestStateToRay.Normal);

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
