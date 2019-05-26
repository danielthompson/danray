package net.danielthompson.danray.integrators;

import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.utility.GeometryCalculations;

/**
 * Created by daniel on 5/5/15.
 */
public class IterativeMISPathTraceIntegrator extends AbstractIntegrator {

   public IterativeMISPathTraceIntegrator(AbstractScene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   public Sample GetSample(Ray ray, int depth, int x, int y) {

      Sample sample = new Sample(x, y);

      float[] fs = new float[maxDepth];
      SpectralPowerDistribution[] spds = new SpectralPowerDistribution[maxDepth + 1];
      ReflectanceSpectrum[] refls = new ReflectanceSpectrum[maxDepth];

      spds[0] = new SpectralPowerDistribution();

      int bounces;

      for (bounces = 0; bounces < maxDepth; bounces++) {

         Intersection closestStateToRay = scene.getNearestShape(ray, x, y);
         if (closestStateToRay == null || !closestStateToRay.hits) {

            spds[bounces].add(scene.Skybox.getSkyBoxSPD(ray.Direction));
            break;
         }

         if (closestStateToRay.shape instanceof AbstractLight) {
            spds[bounces].add(((AbstractLight) closestStateToRay.shape).SpectralPowerDistribution);
            break;
         }

         // get direct lighting contribution

         for (int i = 0; i < scene.Lights.size(); i++) {
            AbstractLight light = scene.Lights.get(i);


         }

         AbstractShape closestShape = closestStateToRay.shape;
         Material objectMaterial = closestShape.Material;
         // TODO fix
         //BRDF brdf = objectMaterial.BRDF;
         Normal intersectionNormal = closestStateToRay.normal;
         Vector3 incomingDirection = ray.Direction;

         // TODO fix
         //if (!brdf.Delta) {
         if (true) {
            for (AbstractLight light : scene.Lights) {
               Point3 intersectionPoint = closestStateToRay.location;

               Point3 lightLocation = light.getRandomPointOnSurface();

               Ray lightToNearestShape = intersectionPoint.createVectorFrom(lightLocation);

               float dot = closestStateToRay.normal.dot(lightToNearestShape.Direction);

               if (dot < 0) {

                  Intersection potentialOccluder = scene.getNearestShape(lightToNearestShape, x, y);

                  if (
                        potentialOccluder == null
                              || !potentialOccluder.hits
                              || potentialOccluder.shape.equals(closestShape)
                              || potentialOccluder.shape.equals(light)
                        ) {
                     float oneOverDistanceFromLightSourceSquared = 1 / lightLocation.squaredDistanceBetween(closestStateToRay.location);

                     Intersection state = closestShape.intersect(lightToNearestShape);
                     if (state.hits) {
                        float angleOfIncidencePercentage = GeometryCalculations.GetCosineWeightedIncidencePercentage(lightToNearestShape.Direction, closestStateToRay.normal);

                        // TODO fix
                        float scalePercentage = 1.0f; //objectMaterial.BRDF.f(incomingDirection, intersectionNormal, lightToNearestShape.Direction) * angleOfIncidencePercentage;
                        SpectralPowerDistribution scaledIncomingSPD = SpectralPowerDistribution.scale(light.SpectralPowerDistribution, scalePercentage);
                        scaledIncomingSPD.scale(oneOverDistanceFromLightSourceSquared);
                        spds[bounces].add(scaledIncomingSPD);
                     }
                  }
               }
            }
         }

         if (bounces + 1 < maxDepth ) {
            // TODO fix

            Vector3 outgoingDirection = new Vector3(1, 0, 0); //objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection, 1, 1);
            float scalePercentage = 1.0f; //objectMaterial.BRDF.f(incomingDirection, intersectionNormal, outgoingDirection);

            fs[bounces] = scalePercentage;
            refls[bounces] = objectMaterial.ReflectanceSpectrum;

            ray = new Ray(closestStateToRay.location, outgoingDirection);
            ray.offsetOriginForward(Constants.HalfEpsilon);
            spds[bounces + 1] = new SpectralPowerDistribution();

         }
      }

      for (int i = bounces - 1; i >= 0; i--) {
         SpectralPowerDistribution spd = spds[i + 1];

         // fix this once direct lighting is added above
         if (refls[i] == null) {
            sample.SpectralPowerDistribution = spd;
            if (spd == null)
               sample.SpectralPowerDistribution = new SpectralPowerDistribution();
            break;
         }

         spd = SpectralPowerDistribution.scale(spd, fs[i]);

         SpectralPowerDistribution reflectedSPD = spd.reflectOff(refls[i]);

         spds[i].add(reflectedSPD);
      }

      sample.SpectralPowerDistribution = spds[0];
      return sample;
   }

}
