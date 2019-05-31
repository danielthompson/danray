package net.danielthompson.danray.integrators;

import net.danielthompson.danray.Logger;
import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shading.bxdf.BxDF;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;

/**
 * Created by daniel on 5/5/15.
 */
public class PathTraceIntegrator extends AbstractIntegrator {

   private int _x;
   private int _y;

   public PathTraceIntegrator(final AbstractScene scene, final int maxDepth) {
      super(scene, maxDepth);
   }

   public Sample getSample(final Ray ray, final int depth, final int x, final int y) {
      _x = x;
      _y = y;

      if (false) {
         if (_x == 360 && _y == 220) {
            return getSample(ray, depth, 1.0f);
         }
         Sample sample = new Sample(x, y);
         sample.SpectralPowerDistribution = new SpectralPowerDistribution(0, 0, 0);
         return sample;
      }
      else {
         return getSample(ray, depth, 1.0f);
      }
   }

   private Sample getSample(final Ray ray, final int depth, final float indexOfRefraction) {

      Logger.Log(Logger.Level.Debug, depth, ray);
      final Sample sample = new Sample(_x, _y);
      Intersection intersection = scene.getNearestShape(ray, _x, _y);

      if (intersection == null || !intersection.hits) {
         Logger.Log(Logger.Level.Debug, depth, "hits: " + false);

         if (intersection != null) {
            sample.KDHeatCount = intersection.KDHeatCount;
         }
         sample.SpectralPowerDistribution = scene.getEnvironmentColor(ray.Direction);
         return sample;
      }

      Logger.Log(Logger.Level.Debug, depth, "hits: " + intersection.hits);
      Logger.Log(Logger.Level.Debug, depth, intersection.location);
      Logger.Log(Logger.Level.Debug, depth, intersection.normal);

      intersection.x = _x;
      intersection.y = _y;

      if (intersection.shape instanceof AbstractLight) {
         sample.SpectralPowerDistribution = ((AbstractLight) intersection.shape).SpectralPowerDistribution;
         return sample;
      }

      sample.SpectralPowerDistribution = new SpectralPowerDistribution();

      // base case
      if (depth >= maxDepth || intersection.shape == null) {
         return sample;
      }
      else {
         AbstractShape closestShape = intersection.shape;
         Material objectMaterial = closestShape.Material;
         Normal intersectionNormal = intersection.normal;
         Vector3 incomingDirection = ray.Direction;

         for (int i = 0; i < objectMaterial.BxDFs.size(); i++) {
            BxDF bxdf = objectMaterial.BxDFs.get(i);
            float weight = objectMaterial.Weights.get(i);

            boolean adjustOutwards = false;

            boolean leavingMaterial = true;
            float enteringIndexOfRefraction = objectMaterial.IndexOfRefraction;

            if (bxdf.Transmission) {
               leavingMaterial = intersectionNormal.dot(incomingDirection) > 0;
               //adjustOutwards = false;

               if (leavingMaterial) {
                  enteringIndexOfRefraction = 1f;
                  adjustOutwards = true;
               }
            }

            // TODO adjust intersection point based on error, transmissive, and normal
            if (adjustOutwards) {
               intersection.location = intersection.shape.offsetRayOrigin(intersection.location, intersection.error, intersectionNormal);
            }
            else {
               Normal n = Normal.scale(intersectionNormal, -1);
               intersection.location = intersection.shape.offsetRayOrigin(intersection.location, intersection.error, n);
            }

            Vector3 outgoingDirection = bxdf.getVectorInPDF(intersection, incomingDirection, indexOfRefraction, enteringIndexOfRefraction);
            float scalePercentage = 1.0f;

            if (!bxdf.Delta) {
               scalePercentage = bxdf.f(incomingDirection, intersectionNormal, outgoingDirection);
            }

            Ray bounceRay = new Ray(intersection.location, outgoingDirection);
            bounceRay.transmissive = adjustOutwards;

            Sample nextSample = getSample(bounceRay, depth + 1, indexOfRefraction);
            SpectralPowerDistribution nextSPD = nextSample.SpectralPowerDistribution;
            nextSPD = SpectralPowerDistribution.scale(nextSPD, scalePercentage);

            ReflectanceSpectrum reflectanceSpectrum = objectMaterial.ReflectanceSpectrum;

            // compute the interaction of the incoming SPD with the object's SRC
            if (objectMaterial.Texture != null) {
               // intersection must compute u and v!
               reflectanceSpectrum = objectMaterial.Texture.Evaluate(intersection.u, intersection.v);
            }
            SpectralPowerDistribution filteredSPD = nextSPD.reflectOff(reflectanceSpectrum);
            filteredSPD.scale(weight);
            sample.SpectralPowerDistribution.add(filteredSPD);
         }
         return sample;
      }
   }
}
