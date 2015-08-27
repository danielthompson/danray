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

import java.util.ArrayList;


/**
 * Created by daniel on 5/5/15.
 */
public class SpectralBDPathTracer extends SpectralTracer {

   public SpectralBDPathTracer(Scene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   //private final double factor = 1.0;
   //private final double iterations = 1.0;
   //private final double adjustment = factor / iterations;

   private final int k = 2;

   public SpectralPowerDistribution GetSPDForRay(Ray ray, int depth) {

      IntersectionState closestStateToRay = _scene.GetClosestDrawableToRay(ray);

      // base cases

      if (closestStateToRay == null) {
         // if we hit nothing, return nothing
         return new SpectralPowerDistribution();
      }

      if (closestStateToRay.Drawable instanceof SpectralRadiatable) {
         // if we hit a light, return the light
         return ((SpectralRadiatable) closestStateToRay.Drawable).getSpectralPowerDistribution();
      }

      // now, we know this ray has hit a surface. so, we now generate light paths.

      /// GET DIRECT LIGHTING CONTRIBUTION ///

      SpectralPowerDistribution directSPD = new SpectralPowerDistribution();
      double directLightPathWeight = 0;

      for (SpectralRadiatable radiatable : _scene.SpectralRadiatables) {
         Point intersectionPoint = closestStateToRay.IntersectionPoint;

         Point radiatableLocation = radiatable.getRandomPointOnSideOf(intersectionPoint);

         Ray lightRayFromCurrentRadiatableToClosestDrawable = intersectionPoint.CreateVectorFrom(radiatableLocation);

         // move the origin slightly outwards, in case we self-intersect
         Point origin = lightRayFromCurrentRadiatableToClosestDrawable.Origin;
         Vector direction = lightRayFromCurrentRadiatableToClosestDrawable.Direction;
         Vector offset = Vector.Scale(direction, -.0000001);

         IntersectionState potentialOccluder = _scene.GetClosestDrawableToRay(lightRayFromCurrentRadiatableToClosestDrawable);

         boolean noOccluder = (potentialOccluder == null);
         boolean targetIntersection = (potentialOccluder.Drawable.equals(closestStateToRay.Drawable) && Constants.WithinEpsilon(potentialOccluder.IntersectionPoint, closestStateToRay.IntersectionPoint));
         boolean shadowRayHitLight = potentialOccluder.Drawable.equals(radiatable);

         if (shadowRayHitLight) {
            potentialOccluder = _scene.GetClosestDrawableToRayBeyond(lightRayFromCurrentRadiatableToClosestDrawable, potentialOccluder.TMin);
         }

         noOccluder = (potentialOccluder == null);
         targetIntersection = (potentialOccluder.Drawable.equals(closestStateToRay.Drawable) && Constants.WithinEpsilon(potentialOccluder.IntersectionPoint, closestStateToRay.IntersectionPoint));
         shadowRayHitLight = potentialOccluder.Drawable.equals(radiatable);

         if (shadowRayHitLight) {
            // shit.. now what?
            ;
         }

         if (noOccluder || targetIntersection) {
            IntersectionState state = closestStateToRay.Drawable.GetHitInfo(lightRayFromCurrentRadiatableToClosestDrawable);
            if (state.Hits) {
               // figure out how much light is shining by sampling the light
               double pdf = radiatable.getPDF(intersectionPoint, direction);

               double pdfPercentage = (4 * Math.PI) / pdf;
               directLightPathWeight = pdfPercentage;
               SpectralPowerDistribution currentIncomingSPD = radiatable.getSpectralPowerDistribution();
               currentIncomingSPD = SpectralPowerDistribution.scale(currentIncomingSPD, pdfPercentage);
               directSPD.add(currentIncomingSPD);

            }
         }
         else {
            directLightPathWeight = 0;
         }
      }

      /// GENERATE LIGHT BOUNCE PATHS ///

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

         final int maxIterations = 4;
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

      /// GENERATE EYE BOUNCE PATHS ///

      ArrayList<LightPath> eyePaths = new ArrayList<>();
      for (int i = 0; i < 1; i++) {
         LightPath l = new LightPath();
         
      }

      /// COMBINE

      //double lightPathWeight = 0;
      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution();

      lightSPD.add(directSPD);

      for (LightPath path : lightPaths) {

         if (path == null || path.surfacePoint == null) {
            // if it didn't hit anything, then we should move on
            continue;
         }
         Point lightBouncePoint = path.surfacePoint;
         Point intersectionPoint = closestStateToRay.IntersectionPoint;

         Ray lightRay = intersectionPoint.CreateVectorFrom(lightBouncePoint);
         lightRay.OffsetOriginForward(1);
         // check to see if the ray hits anything

         Vector incomingDirection = lightRay.Direction;
         Normal surfaceNormal = closestStateToRay.Normal;
         Vector outgoingDirection = Vector.Scale(ray.Direction, -1);

         double brdf = closestStateToRay.Drawable.GetMaterial().BRDF.f(incomingDirection, surfaceNormal, outgoingDirection);

         // if any light is getting reflected back in our initial direction, check to see if there are any occluders
         if (brdf > 0) {
            IntersectionState potentialOccluder = _scene.GetClosestDrawableToRay(lightRay);

            if (potentialOccluder == null || potentialOccluder.Drawable.equals(closestStateToRay.Drawable)) {
               lightSPD.add(SpectralPowerDistribution.scale(path.incomingSPD, brdf));
            }
         }
      }

      Material objectMaterial = closestStateToRay.Drawable.GetMaterial();

      SpectralReflectanceCurve curve = objectMaterial.SpectralReflectanceCurve;
      SpectralPowerDistribution reflectedSPD = lightSPD.reflectOff(curve);
      return reflectedSPD;

//      for (SpectralRadiatable radiatable : _scene.SpectralRadiatables) {
//         Point intersectionPoint = closestStateToRay.IntersectionPoint;
//
//         for (int i = 0; i < iterations; i++) {
//
//            Point radiatableLocation = radiatable.getRandomPointOnSideOf(intersectionPoint);
//
//            Ray lightRayFromCurrentRadiatableToClosestDrawable = intersectionPoint.CreateVectorFrom(radiatableLocation);
//
//            // move the origin slightly outwards, in case we self-intersect
//            Point origin = lightRayFromCurrentRadiatableToClosestDrawable.Origin;
//            Vector direction = lightRayFromCurrentRadiatableToClosestDrawable.Direction;
//            Vector offset = Vector.Scale(direction, -.0000001);
//
//            IntersectionState potentialOccluder = _scene.GetClosestDrawableToRay(lightRayFromCurrentRadiatableToClosestDrawable);
//
//            boolean noOccluder = (potentialOccluder == null);
//            boolean targetIntersection = (potentialOccluder.Drawable.equals(closestStateToRay.Drawable) && Constants.WithinEpsilon(potentialOccluder.IntersectionPoint, closestStateToRay.IntersectionPoint));
//            boolean shadowRayHitLight = potentialOccluder.Drawable.equals(radiatable);
//
//            if (shadowRayHitLight) {
//               potentialOccluder = _scene.GetClosestDrawableToRayBeyond(lightRayFromCurrentRadiatableToClosestDrawable, potentialOccluder.TMin);
//            }
//
//            noOccluder = (potentialOccluder == null);
//            targetIntersection = (potentialOccluder.Drawable.equals(closestStateToRay.Drawable) && Constants.WithinEpsilon(potentialOccluder.IntersectionPoint, closestStateToRay.IntersectionPoint));
//            shadowRayHitLight = potentialOccluder.Drawable.equals(radiatable);
//
//            if (shadowRayHitLight) {
//               // shit.. now what?
//               ;
//            }
//
//            if (noOccluder || targetIntersection) {
//               IntersectionState state = closestStateToRay.Drawable.GetHitInfo(lightRayFromCurrentRadiatableToClosestDrawable);
//               if (state.Hits) {
//                  // figure out how much light is shining by sampling the light
//                  double pdf = radiatable.getPDF(intersectionPoint, direction);
//
//                  double pdfPercentage = (4 * Math.PI) / pdf;
//                  lightPathWeight = pdfPercentage;
//                  SpectralPowerDistribution currentIncomingSPD = radiatable.getSpectralPowerDistribution();
//                  currentIncomingSPD = SpectralPowerDistribution.scale(currentIncomingSPD, pdfPercentage);
//                  directSPD.add(currentIncomingSPD);
//
//               }
//            }
//            else {
//               lightPathWeight = 0;
//            }
//         }
//      }
//
//      // directSPD now has contribution from direct lighting
//
//      /// GENERATE EYE PATHS ///
//
//      Drawable closestDrawable = closestStateToRay.Drawable;
//      Material objectMaterial = closestDrawable.GetMaterial();
//
//      Normal intersectionNormal = closestStateToRay.Normal;
//      Vector incomingDirection = ray.Direction;
//
//      Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection);
//      double scalePercentage = objectMaterial.BRDF.f(incomingDirection, intersectionNormal, outgoingDirection);
//
//      Ray bounceRay = new Ray(closestStateToRay.IntersectionPoint, outgoingDirection);
//
//      closestStateToRay = _scene.GetClosestDrawableToRay(bounceRay);
//
//      SpectralPowerDistribution indirectSPD = new SpectralPowerDistribution();
//
//      if (closestStateToRay == null) {
//         // indirectSPD should be blank
//      }
//
//      else if (closestStateToRay.Drawable instanceof SpectralRadiatable) {
//
//         indirectSPD = ((SpectralRadiatable) closestStateToRay.Drawable).getSpectralPowerDistribution();
//      }
//
//      indirectSPD = SpectralPowerDistribution.scale(indirectSPD, scalePercentage);
//
//      //indirectSPD = new SpectralPowerDistribution();
//
//      /// COMBINE ////
//
//
//
//      //SpectralPowerDistribution totalIncoming = SpectralPowerDistribution.add(directSPD, indirectSPD);
//      SpectralPowerDistribution totalIncoming = SpectralPowerDistribution.lerp(directSPD, (float)(lightPathWeight), indirectSPD, (float)(1.0f -  lightPathWeight));
//
//      SpectralReflectanceCurve curve = objectMaterial.SpectralReflectanceCurve;
//      SpectralPowerDistribution reflectedSPD = totalIncoming.reflectOff(curve);
//      return reflectedSPD;
   }


   private class LightPath {
      public Vector incomingDirection;
      public SpectralPowerDistribution incomingSPD;
      public Point surfacePoint;
      public BRDF surfaceBRDF;
      public Vector outgoingDirection;
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

      double scalePercentage = 1;

      double pdf = firstLight.getPDF(closestStateToRay.IntersectionPoint, incomingDirection);
      double pdfPercentage = (4 * Math.PI) / pdf;
      scalePercentage *= pdfPercentage;

      Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection);
      scalePercentage *= objectMaterial.BRDF.f(incomingDirection, intersectionNormal, outgoingDirection);

      SpectralPowerDistribution incomingSPD = firstLight.getSpectralPowerDistribution();
      incomingSPD = SpectralPowerDistribution.scale(incomingSPD, scalePercentage);

      SpectralReflectanceCurve curve = objectMaterial.SpectralReflectanceCurve;
      SpectralPowerDistribution outgoingSPD = incomingSPD.reflectOff(curve);

      l.incomingDirection = outgoingDirection;
      l.incomingSPD = outgoingSPD;
      l.surfaceBRDF = objectMaterial.BRDF;
      l.surfacePoint = closestStateToRay.IntersectionPoint;
      l.outgoingDirection = outgoingDirection;

      return l;


   }

   public LightPath RandomWalkLightPath(Ray ray, SpectralPowerDistribution incomingSPD) {
      LightPath l = new LightPath();

      // does the ray hit anything?
      IntersectionState closestStateToRay = _scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null) {
         // if not, we're done
         return null;
      }

      else if (closestStateToRay.Drawable instanceof SpectralRadiatable) {
         // if we hit a light
         System.out.println("Subsequent light ray hit light source. Returning null.");
         return null;
      }

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

         l.incomingDirection = outgoingDirection;
         l.incomingSPD = outgoingSPD;
         l.surfaceBRDF = objectMaterial.BRDF;
         l.surfacePoint = closestStateToRay.IntersectionPoint;
         l.outgoingDirection = outgoingDirection;

         return l;

      }
   }
}