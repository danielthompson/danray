package net.danielthompson.danray.tracers;

import net.danielthompson.danray.lights.SpectralRadiatable;
import net.danielthompson.danray.lights.SpectralSphereLight;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shading.SpectralReflectanceCurve;
import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.shapes.Drawable;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.utility.MonteCarloCalculations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * Generates a sample for the given ray using bidirectional path tracing.
 */
public class SpectralBDPathTracer extends SpectralTracer {

   public SpectralBDPathTracer(Scene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   public SpectralPowerDistribution GetSPDForRay(Ray ray, int depth) {

      IntersectionState closestStateToRay = _scene.GetClosestDrawableToRay(ray);

      // degenerate cases

      if (closestStateToRay == null) {
         // if we hit nothing, return nothing
         return new SpectralPowerDistribution();
      }

      if (closestStateToRay.Drawable instanceof SpectralRadiatable) {
         // if we hit a light, return the light
         return ((SpectralRadiatable) closestStateToRay.Drawable).getSpectralPowerDistribution();
      }

      /// GET DIRECT LIGHTING CONTRIBUTION ///

      SpectralPowerDistribution directSPD = getDirectLightingContribution(closestStateToRay);

      /// GENERATE LIGHT BOUNCE PATHS ///

      ArrayList<LightPath> lightPaths = getLightPaths();

      /// GENERATE EYE BOUNCE PATHS ///

      ArrayList<LightPath> eyePaths = getEyePaths(ray);

      /// COMBINE ALL PATHS ///

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution();

      //lightSPD = combineV1(ray, closestStateToRay, lightPaths);
      lightSPD = combineV2(ray, closestStateToRay, lightPaths, eyePaths);

      lightSPD.scale(.0001);

      //lightSPD.add(directSPD);

      Material objectMaterial = closestStateToRay.Drawable.GetMaterial();

      SpectralReflectanceCurve curve = objectMaterial.SpectralReflectanceCurve;
      SpectralPowerDistribution reflectedSPD = lightSPD.reflectOff(curve);
      return reflectedSPD;

   }

   private SpectralPowerDistribution combineV2(Ray initialRay, IntersectionState closestStateToRay, ArrayList<LightPath> lightPaths, ArrayList<LightPath> eyePaths) {
      SpectralPowerDistribution spd = new SpectralPowerDistribution();

      for (int i = 0; i < eyePaths.size(); i++) {

         LightPath eyePath = eyePaths.get(i);
         SpectralPowerDistribution outgoing = new SpectralPowerDistribution();

         // get the direct contribution for this eye path

         SpectralPowerDistribution direct = getDirectLightingContribution(eyePath.state);

         outgoing.add(direct);

         for (int j = 0; j < lightPaths.size(); j++) {
            LightPath lightPath = lightPaths.get(j);

            // determine if anything is blocking the two points
            Vector connectingDirection = Vector.Minus(eyePath.surfacePoint, lightPath.surfacePoint);
            Point connectingOrigin = eyePath.surfacePoint;
            Ray connectingRay = new Ray(connectingOrigin, connectingDirection);
            double maxT = connectingRay.GetTAtPoint(eyePath.surfacePoint);
            IntersectionState potentialOccluder = _scene.GetClosestDrawableHitBetween(connectingRay, 0, maxT);

            // if nothing occludes, then we should proceed
            if (potentialOccluder == null) {

               // calculate outgoing light
               double outgoingBRDF = lightPath.surfaceBRDF.f(lightPath.incomingDirection, lightPath.surfaceNormal, connectingDirection);
               if (outgoingBRDF <= 0) {
                  continue;
               }

               double incomingBRDF = eyePath.surfaceBRDF.f(connectingDirection, eyePath.surfaceNormal, eyePath.outgoingDirection);
               if (incomingBRDF <= 0) {
                  continue;
               }

               double connectingCosTheta = lightPath.surfaceNormal.Dot(connectingDirection);
               double connectingRadiantIntensityFactorForLambert = MonteCarloCalculations.CosineHemispherePDF(connectingCosTheta, 0);
               //System.out.println("connecting RI: " + connectingRadiantIntensityFactorForLambert);

               if (connectingRadiantIntensityFactorForLambert <= 0) {
                  continue;
               }

               double outgoingCosTheta = connectingDirection.Dot(eyePath.surfaceNormal);
               double outgoingRadiantIntensityFactorForLambert = MonteCarloCalculations.CosineHemispherePDF(outgoingCosTheta, 0);

               //System.out.println("outgoing RI: " + outgoingRadiantIntensityFactorForLambert);
               if (outgoingRadiantIntensityFactorForLambert <= 0) {
                  continue;
               }

               SpectralPowerDistribution connectingSPD = SpectralPowerDistribution.scale(lightPath.incomingSPD, connectingRadiantIntensityFactorForLambert);
               connectingSPD = connectingSPD.reflectOff(lightPath.curve);

               SpectralPowerDistribution outgoingSPD = SpectralPowerDistribution.scale(connectingSPD, outgoingRadiantIntensityFactorForLambert);
               outgoing.add(outgoingSPD);

               //
            }

         }

         spd.add(outgoing);
         spd = spd.reflectOff(eyePath.curve);


      }

      return spd;
   }

   private SpectralPowerDistribution combineV1(Ray initialRay, IntersectionState closestStateToRay, ArrayList<LightPath> lightPaths) {

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution();

      for (LightPath path : lightPaths) {

         if (path == null || path.surfacePoint == null) {
            // if it didn't hit anything, then we should move on
            continue;
         }
         Point lightBouncePoint = path.surfacePoint;
         Point intersectionPoint = closestStateToRay.IntersectionPoint;

         Ray lightRay = intersectionPoint.CreateVectorFrom(lightBouncePoint);
         lightRay.OffsetOriginForward(.01);
         // check to see if the ray hits anything

         Vector incomingDirection = lightRay.Direction;
         Normal surfaceNormal = closestStateToRay.Normal;

         // This is fishy...
         Vector outgoingDirection = Vector.Scale(initialRay.Direction, -1);


         // calculate angle between normal and outgoing
         double cosTheta = path.surfaceNormal.Dot(outgoingDirection);

         double incomingRadiantIntensityFactorForLambert = MonteCarloCalculations.CosineHemispherePDF(cosTheta, 0);

         double brdf = closestStateToRay.Drawable.GetMaterial().BRDF.f(incomingDirection, surfaceNormal, outgoingDirection);

         // if any light is getting reflected back in our initial direction, check to see if there are any occluders
         if (brdf > 0) {
            IntersectionState potentialOccluder = _scene.GetClosestDrawableToRay(lightRay);

            if (potentialOccluder == null || potentialOccluder.Drawable.equals(closestStateToRay.Drawable)) {
               SpectralPowerDistribution scaledSPD = SpectralPowerDistribution.scale(path.incomingSPD, 1);
               scaledSPD.scale(incomingRadiantIntensityFactorForLambert);
               lightSPD.add(scaledSPD);
               //System.out.println("Added SPD with power " + scaledSPD.Power);
            }
         }
      }

      return lightSPD;
   }

   private SpectralPowerDistribution getDirectLightingContribution(IntersectionState closestStateToRay) {

      SpectralPowerDistribution directSPD = new SpectralPowerDistribution();
      for (SpectralRadiatable radiatable : _scene.SpectralRadiatables) {
         Point intersectionPoint = closestStateToRay.IntersectionPoint;

         Point radiatableLocation = radiatable.getRandomPointOnSideOf(intersectionPoint);

         Ray lightRayFromCurrentRadiatableToClosestDrawable = intersectionPoint.CreateVectorFrom(radiatableLocation);

         lightRayFromCurrentRadiatableToClosestDrawable.OffsetOriginForward(.0001);

         IntersectionState potentialOccluder = _scene.GetClosestDrawableToRay(lightRayFromCurrentRadiatableToClosestDrawable);

         boolean noOccluder = (potentialOccluder == null);
         boolean shadowRayHitLight = !noOccluder && potentialOccluder.Drawable.equals(radiatable);

         if (shadowRayHitLight) {
            potentialOccluder = _scene.GetClosestDrawableToRayBeyond(lightRayFromCurrentRadiatableToClosestDrawable, potentialOccluder.TMin);
         }

         noOccluder = (potentialOccluder == null);
         boolean targetIntersection = (potentialOccluder.Drawable.equals(closestStateToRay.Drawable) && Constants.WithinEpsilon(potentialOccluder.IntersectionPoint, closestStateToRay.IntersectionPoint));
         shadowRayHitLight = potentialOccluder.Drawable.equals(radiatable);

         if (shadowRayHitLight) {
            // shit.. now what?
            ;
         }

         if (noOccluder || targetIntersection) {
            IntersectionState state = closestStateToRay.Drawable.GetHitInfo(lightRayFromCurrentRadiatableToClosestDrawable);
            if (state.Hits) {
               // figure out how much light is shining by sampling the light
               double pdf = radiatable.getPDF(intersectionPoint, lightRayFromCurrentRadiatableToClosestDrawable.Direction);

               double pdfPercentage = (4 * Math.PI) / pdf;
               SpectralPowerDistribution currentIncomingSPD = radiatable.getSpectralPowerDistribution();
               currentIncomingSPD = SpectralPowerDistribution.scale(currentIncomingSPD, pdfPercentage);
               directSPD.add(currentIncomingSPD);

            }
         }
         else {
         }
      }
      return directSPD;
   }

   private ArrayList<LightPath> getLightPaths() {

      ArrayList<LightPath> lightPaths = new ArrayList<>();

      for (SpectralRadiatable radiatable : _scene.SpectralRadiatables) {
         // pick a random ray in the light's PDF
         Ray lightRay = radiatable.getRandomRayInPDF();
         SpectralSphereLight l = (SpectralSphereLight)radiatable;

         while (l.Inside(lightRay.Origin)) {
            // move the ray outside of the light, if it's inside
            lightRay.OffsetOriginForward(.01);
         }

         // create the first light path
         LightPath lightPath = WalkFirstLightPath(lightRay, radiatable);
         if (lightPath == null) {
            //System.out.println("null lightpath.");
            continue;
         }
         else
            lightPaths.add(lightPath);

         final int maxIterations = 5;
         int currentIteration = 0;

         while (lightPath.surfacePoint != null && lightPath.outgoingDirection != null && lightPath.incomingSPD != null && currentIteration < maxIterations) {
            Ray nextRay = new Ray(lightPath.surfacePoint, lightPath.outgoingDirection);
            lightPath = RandomWalkLightPath(nextRay, lightPath.incomingSPD);
            if (lightPath == null)
               break;
            else
               lightPaths.add(lightPath);
            currentIteration++;
         }
      }

      return lightPaths;
   }

   private ArrayList<LightPath> getEyePaths(Ray initialRay) {
      ArrayList<LightPath> eyePaths = new ArrayList<>();

      // create the first eye path
      LightPath tempEyePath = RandomWalkEyePath(initialRay);
      if (tempEyePath == null) {
      }
      else {
         eyePaths.add(tempEyePath);

         final int maxIterations = 5;
         int currentIteration = 0;

         while (currentIteration < maxIterations) {
            Ray nextRay = new Ray(tempEyePath.surfacePoint, tempEyePath.incomingDirection);
            tempEyePath = RandomWalkEyePath(nextRay);
            if (tempEyePath == null)
               break;
            else
               eyePaths.add(tempEyePath);
            currentIteration++;
         }
      }

      Collections.reverse(eyePaths);

      return eyePaths;
   }


   private class LightPath {
      public Vector incomingDirection;
      public SpectralPowerDistribution incomingSPD;
      public SpectralReflectanceCurve curve;
      public Point surfacePoint;
      public BRDF surfaceBRDF;
      public IntersectionState state;
      public Normal surfaceNormal;

      /**
       * The outgoing bounce direction calculated from the surface's BRDF.
       */
      public Vector outgoingDirection;

      /**
       * The SPD that would go towards the outgoingDirection.
       */
      public SpectralPowerDistribution outgoingSPD;
   }

   public LightPath WalkFirstLightPath(Ray ray, SpectralRadiatable firstLight) {
      LightPath l = new LightPath();

      // does the ray hit anything?
      IntersectionState closestStateToRay = _scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null) {
         // if not, we're done
         return null;
      }

      else if (closestStateToRay.Drawable instanceof SpectralRadiatable) {
         // if we hit a light
         if (firstLight.equals(closestStateToRay.Drawable)) {
            // we hit ourselves. shit.
            //System.out.println("Initial light ray hit same light source.");
            ray.Direction.Scale(-1);
            closestStateToRay = _scene.GetClosestDrawableToRay(ray);

            if (closestStateToRay == null) {
               // if not, we're done
               //System.out.println("Initial light ray hit nothing after reversing.");
               return null;
            }

            else if (closestStateToRay.Drawable instanceof SpectralRadiatable) {
               // if we hit a light
               if (firstLight.equals(closestStateToRay.Drawable)) {
                  //System.out.println("Initial light ray hit same light source, even after reversing.");
                  return null;
               }
            }
            else {
               //System.out.println("We hit an actual object now.");
               // fall through
            }

         }
         else {
            // we hit another light... what do we do?
            //System.out.println("Initial light ray hit different light source.");
            return null; // TODO
         }
      }

      // we hit an actual object
      Drawable closestDrawable = closestStateToRay.Drawable;
      Material objectMaterial = closestDrawable.GetMaterial();

      Normal intersectionNormal = closestStateToRay.Normal;
      Vector incomingDirection = ray.Direction;

      double pdf = firstLight.getPDF(closestStateToRay.IntersectionPoint, incomingDirection);
      double pdfPercentage = (4 * Math.PI) / pdf;

      SpectralPowerDistribution incomingSPD = firstLight.getSpectralPowerDistribution();
      incomingSPD = SpectralPowerDistribution.scale(incomingSPD, pdfPercentage);

      SpectralReflectanceCurve curve = objectMaterial.SpectralReflectanceCurve;
      SpectralPowerDistribution reflectedSPD = incomingSPD.reflectOff(curve);
      Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection);

      double cosTheta = intersectionNormal.Dot(outgoingDirection);
      double incomingRadiantIntensityFactorForLambert = MonteCarloCalculations.CosineHemispherePDF(cosTheta, 0);
      reflectedSPD = SpectralPowerDistribution.scale(reflectedSPD, incomingRadiantIntensityFactorForLambert);

      l.incomingDirection = ray.Direction;
      l.incomingSPD = incomingSPD;
      l.surfaceBRDF = objectMaterial.BRDF;
      l.curve = curve;
      l.surfaceNormal = intersectionNormal;
      l.surfacePoint = closestStateToRay.IntersectionPoint;
      l.outgoingDirection = outgoingDirection;
      l.outgoingSPD = reflectedSPD;

      return l;
   }


   public LightPath RandomWalkLightPath(Ray ray, SpectralPowerDistribution incomingSPD) {
      LightPath l = new LightPath();

      // does the ray hit anything?
      IntersectionState closestStateToRay = _scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null || closestStateToRay.Drawable instanceof SpectralRadiatable) {
         // if not, we're done
         return null;
      }
/*
      else if (closestStateToRay.Drawable instanceof SpectralRadiatable) {
         // if we hit a light
         //System.out.println("Subsequent light ray hit light source. Returning null.");
         return null;
      }
*/
      else {
         // we hit an actual object
         Drawable closestDrawable = closestStateToRay.Drawable;
         Material objectMaterial = closestDrawable.GetMaterial();

         Normal intersectionNormal = closestStateToRay.Normal;
         Vector incomingDirection = ray.Direction;

         double scalePercentage = 1;

         BRDF brdf = objectMaterial.BRDF;
         Vector outgoingDirection = brdf.getVectorInPDF(intersectionNormal, incomingDirection);
         scalePercentage *= brdf.f(incomingDirection, intersectionNormal, outgoingDirection);

         incomingSPD = SpectralPowerDistribution.scale(incomingSPD, scalePercentage);

         SpectralReflectanceCurve curve = objectMaterial.SpectralReflectanceCurve;
         SpectralPowerDistribution outgoingSPD = incomingSPD.reflectOff(curve);

         double cosTheta = intersectionNormal.Dot(outgoingDirection);
         double incomingRadiantIntensityFactorForLambert = MonteCarloCalculations.CosineHemispherePDF(cosTheta, 0);
         outgoingSPD = SpectralPowerDistribution.scale(outgoingSPD, incomingRadiantIntensityFactorForLambert);

         l.incomingDirection = outgoingDirection;
         l.incomingSPD = outgoingSPD;
         l.curve = curve;
         l.surfaceBRDF = objectMaterial.BRDF;
         l.surfaceNormal = intersectionNormal;
         l.surfacePoint = closestStateToRay.IntersectionPoint;
         l.outgoingDirection = outgoingDirection;

         return l;

      }
   }


   public LightPath RandomWalkEyePath(Ray ray) {
      LightPath l = new LightPath();

      // does the ray hit anything?
      IntersectionState closestStateToRay = _scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null) {
         // if not, we're done
         return null;
      }

      else if (closestStateToRay.Drawable instanceof SpectralRadiatable) {
         // if we hit a light
         //System.out.println("Subsequent light ray hit light source. Returning null.");
         return null;
      }
      else {
         // we hit an actual object
         Drawable closestDrawable = closestStateToRay.Drawable;
         Material objectMaterial = closestDrawable.GetMaterial();

         Normal intersectionNormal = closestStateToRay.Normal;
         Vector incomingDirection = ray.Direction;

         BRDF brdf = objectMaterial.BRDF;
         Vector outgoingDirection = brdf.getVectorInPDF(intersectionNormal, incomingDirection);

         l.incomingDirection = outgoingDirection;
         l.surfaceBRDF = objectMaterial.BRDF;
         l.curve = objectMaterial.SpectralReflectanceCurve;
         l.surfaceNormal = intersectionNormal;
         l.surfacePoint = closestStateToRay.IntersectionPoint;
         l.state = closestStateToRay;
         l.outgoingDirection = ray.Direction;

         return l;

      }
   }
}