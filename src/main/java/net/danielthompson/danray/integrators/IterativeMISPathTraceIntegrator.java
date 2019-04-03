package net.danielthompson.danray.integrators;

import net.danielthompson.danray.lights.ILight;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shading.bxdf.BRDF;
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

      Sample sample = new Sample();

      float[] fs = new float[maxDepth];
      SpectralPowerDistribution[] spds = new SpectralPowerDistribution[maxDepth + 1];
      ReflectanceSpectrum[] refls = new ReflectanceSpectrum[maxDepth];

      spds[0] = new SpectralPowerDistribution();

      int bounces;

      for (bounces = 0; bounces < maxDepth; bounces++) {

         Intersection closestStateToRay = scene.getNearestShape(ray, x, y);
         if (closestStateToRay == null || !closestStateToRay.Hits) {

            spds[bounces].add(scene.Skybox.getSkyBoxSPD(ray.Direction));
            break;
         }

         if (closestStateToRay.Shape instanceof ILight) {
            spds[bounces].add(((ILight) closestStateToRay.Shape).SpectralPowerDistribution);
            break;
         }

         // get direct lighting contribution

         for (int i = 0; i < scene.Lights.size(); i++) {
            ILight light = scene.Lights.get(i);


         }

         AbstractShape closestShape = closestStateToRay.Shape;
         Material objectMaterial = closestShape.Material;
         // TODO fix
         //BRDF brdf = objectMaterial.BRDF;
         Normal intersectionNormal = closestStateToRay.Normal;
         Vector incomingDirection = ray.Direction;

         // TODO fix
         //if (!brdf.Delta) {
         if (true) {
            for (ILight light : scene.Lights) {
               Point intersectionPoint = closestStateToRay.Location;

               Point lightLocation = light.getRandomPointOnSurface();

               Ray lightToNearestShape = intersectionPoint.CreateVectorFrom(lightLocation);

               float dot = closestStateToRay.Normal.Dot(lightToNearestShape.Direction);

               if (dot < 0) {

                  Intersection potentialOccluder = scene.getNearestShape(lightToNearestShape, x, y);

                  if (
                        potentialOccluder == null
                              || !potentialOccluder.Hits
                              || potentialOccluder.Shape.equals(closestShape)
                              || potentialOccluder.Shape.equals(light)
                        ) {
                     float oneOverDistanceFromLightSourceSquared = 1 / lightLocation.SquaredDistanceBetween(closestStateToRay.Location);

                     Intersection state = closestShape.getHitInfo(lightToNearestShape);
                     if (state.Hits) {
                        float angleOfIncidencePercentage = GeometryCalculations.GetCosineWeightedIncidencePercentage(lightToNearestShape.Direction, closestStateToRay.Normal);

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

            Vector outgoingDirection = new Vector(1, 0, 0); //objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection, 1, 1);
            float scalePercentage = 1.0f; //objectMaterial.BRDF.f(incomingDirection, intersectionNormal, outgoingDirection);

            fs[bounces] = scalePercentage;
            refls[bounces] = objectMaterial.ReflectanceSpectrum;

            ray = new Ray(closestStateToRay.Location, outgoingDirection);
            ray.OffsetOriginForward(Constants.HalfEpsilon);
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
