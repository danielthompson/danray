package net.danielthompson.danray.integrators;

import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shading.bxdf.BxDF;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;

import java.io.Console;


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

      return GetSample(ray, depth, 1.0f);
//
//      if (_x == 324 && _y == 361) {
//         return GetSample(ray, depth, 1.0f);
//      }
//
//      Sample sample = new Sample();
//      sample.SpectralPowerDistribution = new SpectralPowerDistribution(0, 0, 0);
//      return sample;

   }

   private Sample GetSample(Ray ray, int depth, float indexOfRefraction) {

      Sample sample = new Sample();
      Intersection intersection = scene.getNearestShape(ray, _x, _y);

      if (intersection == null || !intersection.Hits) {

         if (intersection != null) {
            sample.KDHeatCount = intersection.KDHeatCount;
         }
         sample.SpectralPowerDistribution = scene.getEnvironmentColor(ray.Direction);
         return sample;
      }

      intersection.x = _x;
      intersection.y = _y;

      if (intersection.Shape instanceof AbstractLight) {
         sample.SpectralPowerDistribution = ((AbstractLight) intersection.Shape).SpectralPowerDistribution;
         return sample;
      }

      // base case
      if (depth >= maxDepth || intersection.Shape == null) {
         sample.SpectralPowerDistribution = new SpectralPowerDistribution();
         return sample;
      }
      else {

         sample.SpectralPowerDistribution = new SpectralPowerDistribution();

         AbstractShape closestShape = intersection.Shape;
//         if (closestShape == null) {
//            sample.SpectralPowerDistribution = new SpectralPowerDistribution();
//            return sample;
//         }
         Material objectMaterial = closestShape.Material;

         Normal intersectionNormal = intersection.Normal;
         Vector incomingDirection = ray.Direction;

         for (int i = 0; i < objectMaterial.BxDFs.size(); i++) {
            BxDF bxdf = objectMaterial.BxDFs.get(i);
            float weight = objectMaterial.Weights.get(i);

            boolean leavingMaterial = true;
            float enteringIndexOfRefraction = objectMaterial.IndexOfRefraction;

            if (bxdf.Transmission) {
               leavingMaterial = intersectionNormal.Dot(incomingDirection) > 0;

               if (leavingMaterial) {
                  enteringIndexOfRefraction = 1f;
               }
            }

            Vector outgoingDirection = bxdf.getVectorInPDF(intersection, incomingDirection, indexOfRefraction, enteringIndexOfRefraction);

            float scalePercentage = 1.0f;

            if (!bxdf.Delta) {
               scalePercentage = bxdf.f(incomingDirection, intersectionNormal, outgoingDirection);
            }

            if (leavingMaterial && outgoingDirection.Dot(intersectionNormal) < 0) {
               outgoingDirection.Scale(-1);
            }

            Ray bounceRay = new Ray(intersection.Location, outgoingDirection);



            if (leavingMaterial) {
               bounceRay.OffsetOriginOutwards(intersectionNormal);
            }
            else {
               bounceRay.OffsetOriginInwards(intersectionNormal);
            }

           // bounceRay.OffsetOriginOutwards(intersectionNormal);
            //bounceRay.OffsetOriginForward(Constants.DoubleEpsilon);

            Sample nextSample = GetSample(bounceRay, depth + 1, indexOfRefraction);
            SpectralPowerDistribution nextSPD = nextSample.SpectralPowerDistribution;
            nextSPD = SpectralPowerDistribution.scale(nextSPD, scalePercentage);

            // compute the interaction of the incoming SPD with the object's SRC
            ReflectanceSpectrum reflectanceSpectrum = objectMaterial.ReflectanceSpectrum;
            SpectralPowerDistribution filteredSPD = nextSPD.reflectOff(reflectanceSpectrum);
            filteredSPD.scale(weight);
            sample.SpectralPowerDistribution.add(filteredSPD);
         }
//
//         // reflect
//         if (objectMaterial.BRDF != null) {
//            Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersection, incomingDirection);
//            float scalePercentage = objectMaterial.BRDF.f(incomingDirection, intersectionNormal, outgoingDirection);
//            Ray bounceRay = new Ray(intersection.Location, outgoingDirection);
//
//            bounceRay.OffsetOriginOutwards(intersectionNormal);
//            //bounceRay.OffsetOriginForward(Constants.DoubleEpsilon);
//
//            Sample reflectSample = GetSample(bounceRay, depth + 1, indexOfRefraction);
//            SpectralPowerDistribution incomingSPD = reflectSample.SpectralPowerDistribution;
//            incomingSPD = SpectralPowerDistribution.scale(incomingSPD, scalePercentage);
//
//            // compute the interaction of the incoming SPD with the object's SRC
//            ReflectanceSpectrum reflectanceSpectrum = objectMaterial.ReflectanceSpectrum;
//            SpectralPowerDistribution reflectedSPD = incomingSPD.reflectOff(reflectanceSpectrum);
//            reflectedSPD.scale(objectMaterial.BRDFweight);
//            sample.SpectralPowerDistribution.add(reflectedSPD);
//         }
//
//         // refract
//         if (objectMaterial.BTDF != null) {
//            float leavingIndexOfRefraction = indexOfRefraction;
//            float enteringIndexOfRefraction = objectMaterial.IndexOfRefraction;
//
//            boolean leavingMaterial = intersectionNormal.Dot(incomingDirection) > 0;
//
//            if (leavingMaterial) {
//               enteringIndexOfRefraction = 1f;
//            }
//
//            Vector outgoingDirection = objectMaterial.BTDF.getVectorInPDF(intersectionNormal, incomingDirection, leavingIndexOfRefraction, enteringIndexOfRefraction);
////            Vector outgoingDirection = new Vector(1, 1, 1);
//            //float scalePercentage = objectMaterial.BTDF.f(incomingDirection, intersectionNormal, outgoingDirection);
//            Ray bounceRay = new Ray(intersection.Location, outgoingDirection);
//
//            if (leavingMaterial) {
//               bounceRay.OffsetOriginOutwards(intersectionNormal);
//            }
//            else {
//               bounceRay.OffsetOriginInwards(intersectionNormal);
//            }
//
//            Sample refractSample = GetSample(bounceRay, depth + 1, enteringIndexOfRefraction);
//            SpectralPowerDistribution incomingSPD = refractSample.SpectralPowerDistribution;
//            incomingSPD = SpectralPowerDistribution.scale(incomingSPD, 1.0f);
//
//            // compute the interaction of the incoming SPD with the object's SRC
//            ReflectanceSpectrum reflectanceSpectrum = objectMaterial.ReflectanceSpectrum;
//            SpectralPowerDistribution reflectedSPD = incomingSPD.reflectOff(reflectanceSpectrum);
//            reflectedSPD.scale(objectMaterial.BTDFweight);
//            sample.SpectralPowerDistribution.add(reflectedSPD);
//         }
//
//
//
//         if (objectMaterial.BSSRDF != null) {
//
//            float transmittance = objectMaterial.BSSRDF.Transmittance;
//
//            // initial vector needs to be within 90 degrees of the inverted normal
//            Vector outgoing = objectMaterial.BSSRDF.GetVector();
//
//            if (outgoing.Dot(intersectionNormal) > 0)
//               outgoing.Scale(-1);
//
//            Ray bounceRay = new Ray(intersection.Location, outgoing);
//            boolean hits = closestShape.hits(bounceRay);
//            Intersection nextIntersection = closestShape.getHitInfo(bounceRay);
//            Intersection previousIntersection = nextIntersection;
//
//            // TODO fix t comparisons
//            while (hits && (nextIntersection.t < 0 || nextIntersection.t > 1)) {
//               // bounce it again, sam
//               transmittance *= objectMaterial.BSSRDF.Transmittance;
//               outgoing = objectMaterial.BSSRDF.GetVector();
//               Point newOrigin = objectMaterial.BSSRDF.GetNextPoint(previousIntersection.Location, outgoing);
//               bounceRay = new Ray(newOrigin, outgoing);
//               hits = closestShape.hits(bounceRay);
//               previousIntersection = nextIntersection;
//               nextIntersection = closestShape.getHitInfo(bounceRay);
//            }
//            // exiting
//
//            bounceRay = new Ray(previousIntersection.Location, bounceRay.Direction);
//            bounceRay.OffsetOriginOutwards(previousIntersection.Normal);
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
