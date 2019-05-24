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

   public PathTraceIntegrator(AbstractScene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   public Sample GetSample(Ray ray, int depth, int x, int y) {
      _x = x;
      _y = y;

      if (false) {
//         if ((_x > 318 && _x < 322) && (_y > 30 && _y < 34)) {
         if (_x == 320 && _y == 186) {
            return GetSample(ray, depth, 1.0f);
         }
         Sample sample = new Sample(x, y);
         sample.SpectralPowerDistribution = new SpectralPowerDistribution(0, 0, 0);
         return sample;
      }
      else {
         return GetSample(ray, depth, 1.0f);
      }

   }

   private Sample GetSample(Ray ray, int depth, float indexOfRefraction) {

      Logger.Log(Logger.Level.Debug, depth, ray);
      Sample sample = new Sample(_x, _y);
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

            boolean leavingMaterial = true;
            float enteringIndexOfRefraction = objectMaterial.IndexOfRefraction;

            if (bxdf.Transmission) {
               leavingMaterial = intersectionNormal.dot(incomingDirection) > 0;

               if (leavingMaterial) {
                  enteringIndexOfRefraction = 1f;
               }
            }

            Vector3 outgoingDirection = bxdf.getVectorInPDF(intersection, incomingDirection, indexOfRefraction, enteringIndexOfRefraction);
            float scalePercentage = 1.0f;

            if (!bxdf.Delta) {
               scalePercentage = bxdf.f(incomingDirection, intersectionNormal, outgoingDirection);
            }
//            if (leavingMaterial && outgoingDirection.dot(intersectionNormal) < 0) {
//               //outgoingDirection.scale(-1);
//            }

            Ray bounceRay = new Ray(intersection.location, outgoingDirection);
            if (leavingMaterial) {
               //bounceRay.offsetOriginOutwards(intersectionNormal);
            }
            else {
               //bounceRay.offsetOriginInwards(intersectionNormal);
            }

            Sample nextSample = GetSample(bounceRay, depth + 1, indexOfRefraction);
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
//         if (objectMaterial.BSSRDF != null) {
//
//            float transmittance = objectMaterial.BSSRDF.Transmittance;
//
//            // initial vector needs to be within 90 degrees of the inverted normal
//            Vector outgoing = objectMaterial.BSSRDF.GetVector();
//
//            if (outgoing.dot(intersectionNormal) > 0)
//               outgoing.scale(-1);
//
//            Ray bounceRay = new Ray(intersection.location, outgoing);
//            boolean hits = closestShape.hits(bounceRay);
//            intersection nextIntersection = closestShape.getHitInfo(bounceRay);
//            intersection previousIntersection = nextIntersection;
//
//            // TODO fix t comparisons
//            while (hits && (nextIntersection.t < 0 || nextIntersection.t > 1)) {
//               // bounce it again, sam
//               transmittance *= objectMaterial.BSSRDF.Transmittance;
//               outgoing = objectMaterial.BSSRDF.GetVector();
//               Point newOrigin = objectMaterial.BSSRDF.GetNextPoint(previousIntersection.location, outgoing);
//               bounceRay = new Ray(newOrigin, outgoing);
//               hits = closestShape.hits(bounceRay);
//               previousIntersection = nextIntersection;
//               nextIntersection = closestShape.getHitInfo(bounceRay);
//            }
//            // exiting
//
//            bounceRay = new Ray(previousIntersection.location, bounceRay.Direction);
//            bounceRay.offsetOriginOutwards(previousIntersection.normal);
//
//            Sample bssrdfSample = GetSample(bounceRay, depth + 1, indexOfRefraction);
//            SpectralPowerDistribution incomingSPD = bssrdfSample.SpectralPowerDistribution;
//            incomingSPD = SpectralPowerDistribution.scale(incomingSPD, transmittance);
//
//            // compute the interaction of the incoming SPD with the object's SRC
//            ReflectanceSpectrum reflectanceSpectrum = objectMaterial.ReflectanceSpectrum;
//            SpectralPowerDistribution reflectedSPD = incomingSPD.reflectOff(reflectanceSpectrum);
//            reflectedSPD.scale(objectMaterial.BSSRDFweight);
//            sample.SpectralPowerDistribution.add(reflectedSPD);
//         }
         return sample;
      }
   }
}
