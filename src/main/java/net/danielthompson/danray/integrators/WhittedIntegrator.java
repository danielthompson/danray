package net.danielthompson.danray.integrators;

import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;

import net.danielthompson.danray.structures.Point3;
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
   public Sample GetSample(Ray ray, int depth, int x, int y) {
      return GetSample(ray, depth, _airIndexOfRefraction, x, y);
   }

   private Sample GetSample(Ray ray, int depth, float oldIndexOfRefraction, int x, int y) {

      Sample sample = new Sample();

      if (x == 180 && y == 230) {
         int i = 0;
      }

      Intersection closestStateToRay = scene.getNearestShape(ray, x, y);

      if (closestStateToRay == null || !closestStateToRay.Hits) {

         if (closestStateToRay != null)
            sample.KDHeatCount = closestStateToRay.KDHeatCount;
         sample.SpectralPowerDistribution = scene.Skybox.getSkyBoxSPD(ray.Direction);
         return sample;
      }

      sample.KDHeatCount = closestStateToRay.KDHeatCount;

      if (closestStateToRay.Shape instanceof AbstractLight) {
         sample.SpectralPowerDistribution = ((AbstractLight)closestStateToRay.Shape).SpectralPowerDistribution;
         return sample;
      }

      AbstractShape closestShape = closestStateToRay.Shape;

      Material objectMaterial = closestShape.Material;

      //BRDF brdf = objectMaterial.BRDF;

      SpectralPowerDistribution directSPD = new SpectralPowerDistribution();

      // calculate direct light

//      if (brdf.Delta) {
      if (false) {

      }

      else {

         for (AbstractLight light : scene.Lights) {
            Point3 intersectionPoint = closestStateToRay.Location;

            Point3 lightLocation = light.getRandomPointOnSurface();

            Ray lightToNearestShape = intersectionPoint.createVectorFrom(lightLocation);

            float dot = closestStateToRay.Normal.Dot(lightToNearestShape.Direction);

            if (dot < 0) {

               Intersection potentialOccluder = scene.getNearestShape(lightToNearestShape, x, y);

               if (
                     potentialOccluder == null
                           || !potentialOccluder.Hits
                           || potentialOccluder.Shape.equals(closestShape)
                           || potentialOccluder.Shape.equals(light)
                     ) {
                  float oneOverDistanceFromLightSourceSquared = 1 / lightLocation.squaredDistanceBetween(closestStateToRay.Location);

                  Intersection state = closestShape.GetHitInfo(lightToNearestShape);
                  if (state.Hits) {
                     float angleOfIncidencePercentage = GeometryCalculations.GetCosineWeightedIncidencePercentage(lightToNearestShape.Direction, closestStateToRay.Normal);
                     SpectralPowerDistribution scaledIncomingSPD = SpectralPowerDistribution.scale(light.SpectralPowerDistribution, angleOfIncidencePercentage);
                     scaledIncomingSPD.scale(oneOverDistanceFromLightSourceSquared);
                     directSPD.add(scaledIncomingSPD);
                  }
               }
            }
         }
      }
      directSPD = directSPD.reflectOff(objectMaterial.ReflectanceSpectrum);

      if (x == 476 && y == 531) { // out
         int j = 0;
         depth++;
         depth--;
      }

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

         SpectralPowerDistribution reflectedSPD = null;

         // TODO fix
         if (true) {
         //if (objectMaterial.BRDF != null) {

//            if (x == 555 && y == 644) { // inside
//               int j = 0;
//               Vector outgoingDirection = objectMaterial.reflect.getVectorInPDF(closestStateToRay.Normal, ray.Direction);
//            }


            // TODO fix
            Vector outgoingDirection = new Vector(1, 0, 0); //objectMaterial.BRDF.getVectorInPDF(closestStateToRay.Normal, ray.Direction, 1, 1);

            Point3 offsetIntersection = Point3.plus(closestStateToRay.Location, Vector.Scale(outgoingDirection, Constants.Epsilon * 1000));
//            Point offsetIntersection = closestStateToRay.Location;



            Ray reflectedRay = new Ray(offsetIntersection, outgoingDirection);

            reflectedSample = GetSample(reflectedRay, depth, oldIndexOfRefraction, x, y);

            // TODO fix
//            if (objectMaterial.BRDF.Delta) {
            if (true) {
               reflectedWeight = 1.0f;
            }

            else {

               Vector reversedIncoming = Vector.Scale(ray.Direction, -1);

               float angleIncoming = GeometryCalculations.radiansBetween(reversedIncoming, closestStateToRay.Normal);
               float angleOutgoing = GeometryCalculations.radiansBetween(outgoingDirection, closestStateToRay.Normal);

               // TODO fix
               reflectedWeight = 1.0f; //objectMaterial.BRDF.f(angleIncoming, angleOutgoing);

            }

            reflectedSPD = SpectralPowerDistribution.scale(reflectedSample.SpectralPowerDistribution, reflectedWeight);

//            reflectedSPD = reflectedSample.SpectralPowerDistribution.reflectOff(objectMaterial.ReflectanceSpectrum);
            reflectedSPD = reflectedSPD.reflectOff(objectMaterial.ReflectanceSpectrum);

         }

         Sample refractedSample = null;
         /*
         else if (objectMaterial.getReflectivity() > 0) {
            Ray reflectedRay = GeometryCalculations.GetReflectedRay(closestStateToRay.Location, closestStateToRay.Normal, ray);

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
         //float transparency = (float)objectMaterial._transparency;


         Sample s = new Sample();
         s.SpectralPowerDistribution = SpectralPowerDistribution.add(directSPD, reflectedSPD);

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
