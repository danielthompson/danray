package net.danielthompson.danray.integrators;

import net.danielthompson.danray.lights.SpectralRadiatable;
import net.danielthompson.danray.lights.SpectralSphereLight;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shading.SpectralReflectanceCurve;
import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.shapes.Shape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.utility.MonteCarloCalculations;

import java.util.ArrayList;

/**
 * Generates a sample for the given ray using bidirectional path tracing.
 */
public class SpectralBDPathTracer extends AbstractIntegrator {

   private int numFixedLightVertices = -1;
   private int numFixedEyeVertices = -1;

   public SpectralBDPathTracer(Scene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   public void setDebug(int s, int t) {
      numFixedLightVertices = s;
      numFixedEyeVertices = t;
   }

   public SpectralPowerDistribution GetSPDForRay(Ray ray, int depth) {
      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution();
      IntersectionState closestStateToRay = scene.GetClosestDrawableToRay(ray);

      // degenerate cases

      if (closestStateToRay == null) {
         // if we hit nothing, return nothing
         return new SpectralPowerDistribution();
      }

      if (closestStateToRay.Shape instanceof SpectralRadiatable) {
         // if we hit a light, return the light
         return ((SpectralRadiatable) closestStateToRay.Shape).getSpectralPowerDistribution();
      }

      /// GET DIRECT LIGHTING CONTRIBUTION ///

      //SpectralPowerDistribution directSPD = getDirectLightingContribution(closestStateToRay);

      /// GENERATE LIGHT BOUNCE PATHS ///

      ArrayList<LightVertex> lightVertices = getLightVertices();

      if (lightVertices.size() > numFixedLightVertices) {
         ;
      }

      /// GENERATE EYE BOUNCE PATHS ///

      ArrayList<LightVertex> eyeVertices = getEyeVertices(ray);

      int i = 10;

      while (eyeVertices.size() != numFixedEyeVertices) {
         eyeVertices = getEyeVertices(ray);
         if (i-- == 0)
            return lightSPD;
      }

      /// combine into passages

      //ArrayList<LightPassage> lightPassages = getLightPassages(lightPaths, eyePaths);

      //SpectralPowerDistribution passageSPD = combinePassages(lightPassages);

      /// COMBINE ALL PATHS ///



      //lightSPD = combineV1(ray, closestStateToRay, lightPaths);
      lightSPD = combineV2(lightVertices, eyeVertices);

      //lightSPD.scale(.0001);

      //lightSPD.add(directSPD);

      Material objectMaterial = closestStateToRay.Shape.GetMaterial();

      SpectralReflectanceCurve curve = objectMaterial.SpectralReflectanceCurve;
      SpectralPowerDistribution reflectedSPD = lightSPD.reflectOff(curve);
      return reflectedSPD;

   }

   private SpectralPowerDistribution combinePassages(ArrayList<LightPassage> passages) {

      SpectralPowerDistribution spd = new SpectralPowerDistribution();

      for (LightPassage passage : passages) {
         passage.p = 1;
         for (LightVertex vertex : passage.vertices) {
            passage.p *= vertex.calculatedPDF;

         }
      }

      return null;

   }

   public ArrayList<LightPassage> getLightPassages(ArrayList<LightVertex> lightPaths, ArrayList<LightVertex> eyePaths) {

      ArrayList<LightPassage> passages = new ArrayList<>();

      for (int i = 0; i < eyePaths.size(); i++) {

         LightVertex eyePath = eyePaths.get(i);

         for (int j = 0; j < lightPaths.size(); j++) {
            LightVertex lightPath = lightPaths.get(j);

            // determine if anything is blocking the two points
            Vector connectingDirection = Vector.Minus(eyePath.surfacePoint, lightPath.surfacePoint);
            connectingDirection.Normalize();
            Point connectingOrigin = eyePath.surfacePoint;
            Ray connectingRay = new Ray(connectingOrigin, connectingDirection);
            double maxT = connectingRay.GetTAtPoint(eyePath.surfacePoint);
            IntersectionState potentialOccluder = scene.GetClosestDrawableHitBetween(connectingRay, 0, maxT);

            // if nothing occludes, then we should proceed
            if (potentialOccluder == null) {

               // calculate outgoing light
               double outgoingBRDF = lightPath.surfaceBRDF.f(lightPath.incomingDirection, lightPath.surfaceNormal, connectingDirection);
               if (outgoingBRDF <= 0) {
                  continue;
               }
               lightPath.calculatedPDF = outgoingBRDF;

               double incomingBRDF = eyePath.surfaceBRDF.f(connectingDirection, eyePath.surfaceNormal, eyePath.outgoingDirection);
               if (incomingBRDF <= 0) {
                  continue;
               }
               eyePath.calculatedPDF = incomingBRDF;

               double probability = outgoingBRDF * incomingBRDF;

               if (probability > 0) {
                  LightPassage passage = new LightPassage();
                  passage.vertices = new ArrayList<>();
                  passage.vertices.add(lightPath);
                  passage.vertices.add(eyePath);
                  passages.add(passage);
               }

               /*
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
               */
               //SpectralPowerDistribution outgoingSPD = SpectralPowerDistribution.scale(connectingSPD, outgoingRadiantIntensityFactorForLambert);
               //outgoing.add(outgoingSPD);

               //
            }

         }

         //spd.add(outgoing);
         //spd = spd.reflectOff(eyePath.curve);


      }

      return null;
   }

   public SpectralPowerDistribution combineV2(ArrayList<LightVertex> lightVertices, ArrayList<LightVertex> eyeVertices) {
      SpectralPowerDistribution vertexSPD = new SpectralPowerDistribution();

      for (int i = eyeVertices.size() - 1; i >= 1; i--) {

         LightVertex eyeVertex = eyeVertices.get(i);

         SpectralPowerDistribution outgoing = new SpectralPowerDistribution();

         // get direct contribution

        // for (int j = 0; j < lightVertices.size(); j++) {

         if (lightVertices.size() > 0) {
            // the first light vertex should be a point on a light.
            // therefore, we should be able to calculate the direct lighting contribution to eyeVertices[i].

            // check to see if the light's pdf indicates it will shine in the direction of the eye vertex

            LightVertex lightVertex = lightVertices.get(0);
            SpectralRadiatable light = lightVertex.radiatable;
            Vector directionFromLightToPoint = Vector.Minus(eyeVertex.surfacePoint, lightVertex.surfacePoint);
            double lightDensityTowardsPoint = light.getPDF(eyeVertex.surfacePoint, directionFromLightToPoint);
//            outgoing = RelativeSpectralPowerDistributionLibrary.Blue.getSPD();
//            outgoing.scale(lightDensityTowardsPoint * 1000);
//            return outgoing;
            if (lightDensityTowardsPoint > 0) {
               // check to see if the eye vertex's BRDF will reflect anything from the light back to the previous eye vertex
               Vector outgoingDirection = Vector.Scale(eyeVertex.incomingDirection, -1);
               outgoingDirection.Normalize();
               double brdfPDF = eyeVertex.surfaceBRDF.f(directionFromLightToPoint, eyeVertex.surfaceNormal, outgoingDirection);
//               outgoing = RelativeSpectralPowerDistributionLibrary.Blue.getSPD();
//               outgoing.scale(brdfPDF * 10);
//               return outgoing;
               if (brdfPDF > 0 ) {
                  Ray connectingRay = new Ray(lightVertex.surfacePoint, directionFromLightToPoint);
                  double maxT = connectingRay.GetTAtPoint(eyeVertex.surfacePoint);
                  IntersectionState potentialOccluder = scene.GetClosestDrawableHitBetween(connectingRay, 0, maxT);
//                  SpectralPowerDistribution red = RelativeSpectralPowerDistributionLibrary.Red.getSPD();
//                  SpectralPowerDistribution blue = RelativeSpectralPowerDistributionLibrary.Blue.getSPD();
//
//                  red.scale((potentialOccluder == null) ? 0 : 10);
//
//                  if (potentialOccluder == null) {
//                     blue.scale(0);
//                  } else {
//                     blue.scale(potentialOccluder.Drawable == light ? 10 : 0);
//                  }
//
//                  outgoing.add(blue);
//                  outgoing.add(red);
//
//
//                  return outgoing;
                  if (potentialOccluder == null) {
                     double cosOutgoing = eyeVertex.surfaceNormal.Dot(eyeVertex.incomingDirection);
                     double factor = cosOutgoing * brdfPDF * lightDensityTowardsPoint;
                     SpectralPowerDistribution directSPD = SpectralPowerDistribution.scale(light.getSpectralPowerDistribution(), factor);
                     //directSPD.reflectOff(eyeVertex.curve);
                     vertexSPD.add(directSPD.reflectOff(eyeVertex.curve));
                  }
               }
            }
         }

         LightVertex previousEyeVertex = null;
         SpectralPowerDistribution previousVertexOutgoingSPD = null;

         if (i < eyeVertices.size() - 1 && i >= 1) {
            previousEyeVertex = eyeVertices.get(i + 1);
            previousVertexOutgoingSPD = SpectralPowerDistribution.scale(vertexSPD, 1);
            if (previousEyeVertex == null) {
               int foo = 0;
            }
         }

         if (previousEyeVertex != null) {


         }

//         /*
//         for (int j = 1; j < lightVertices.size(); j++) {
//            LightVertex lightPath = lightVertices.get(j);
//
//            // determine if anything is blocking the two points
//            Vector connectingDirection = Vector.Minus(eyeVertex.surfacePoint, lightPath.surfacePoint);
//            connectingDirection.Normalize();
//            Point connectingOrigin = lightPath.surfacePoint;
//            Ray connectingRay = new Ray(connectingOrigin, connectingDirection);
//            double maxT = connectingRay.GetTAtPoint(eyeVertex.surfacePoint);
//            IntersectionState potentialOccluder = scene.GetClosestDrawableHitBetween(connectingRay, 0, maxT);
//
//            // if nothing occludes, then we should proceed
//            if (potentialOccluder == null) {
//
//               // calculate outgoing light
//               double outgoingBRDF = lightPath.surfaceBRDF.f(lightPath.incomingDirection, lightPath.surfaceNormal, connectingDirection);
//               if (outgoingBRDF <= 0) {
//                  continue;
//               }
//
//               double incomingBRDF = eyeVertex.surfaceBRDF.f(connectingDirection, eyeVertex.surfaceNormal, eyeVertex.outgoingDirection);
//               if (incomingBRDF <= 0) {
//                  continue;
//               }
//
//               double connectingCosTheta = lightPath.surfaceNormal.Dot(connectingDirection);
//               double connectingRadiantIntensityFactorForLambert = MonteCarloCalculations.CosineHemispherePDF(connectingCosTheta, 0);
//               //System.out.println("connecting RI: " + connectingRadiantIntensityFactorForLambert);
//
//               if (connectingRadiantIntensityFactorForLambert <= 0) {
//                  continue;
//               }
//
//               double outgoingCosTheta = connectingDirection.Dot(eyeVertex.surfaceNormal);
//               double outgoingRadiantIntensityFactorForLambert = MonteCarloCalculations.CosineHemispherePDF(outgoingCosTheta, 0);
//
//               //System.out.println("outgoing RI: " + outgoingRadiantIntensityFactorForLambert);
//               if (outgoingRadiantIntensityFactorForLambert <= 0) {
//                  continue;
//               }
//
//               SpectralPowerDistribution connectingSPD = SpectralPowerDistribution.scale(lightPath.incomingSPD, connectingRadiantIntensityFactorForLambert);
//               connectingSPD = connectingSPD.reflectOff(lightPath.curve);
//
//               SpectralPowerDistribution outgoingSPD = SpectralPowerDistribution.scale(connectingSPD, outgoingRadiantIntensityFactorForLambert);
//               outgoing.add(outgoingSPD);
//
//               //
//            }
//
//         }*/
         //vertexSPD.add(outgoing);
         if (eyeVertex.curve == null) {
            int foo = 2;
         }

         //vertexSPD = vertexSPD.reflectOff(eyeVertex.curve);


      }

      return vertexSPD;
   }


   public SpectralPowerDistribution combineV1(Ray initialRay, IntersectionState closestStateToRay, ArrayList<LightVertex> lightPaths) {

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution();

      for (LightVertex path : lightPaths) {

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

         double brdf = closestStateToRay.Shape.GetMaterial().BRDF.f(incomingDirection, surfaceNormal, outgoingDirection);

         // if any light is getting reflected back in our initial direction, check to see if there are any occluders
         if (brdf > 0) {
            IntersectionState potentialOccluder = scene.GetClosestDrawableToRay(lightRay);

            if (potentialOccluder == null || potentialOccluder.Shape.equals(closestStateToRay.Shape)) {
               SpectralPowerDistribution scaledSPD = SpectralPowerDistribution.scale(path.incomingSPD, 1);
               scaledSPD.scale(incomingRadiantIntensityFactorForLambert);
               lightSPD.add(scaledSPD);
               //System.out.println("Added SPD with power " + scaledSPD.Power);
            }
         }
      }

      return lightSPD;
   }

   public SpectralPowerDistribution getDirectLightingContribution(LightVertex vertex) {

      IntersectionState closestStateToRay = vertex.state;
      Point surfacePoint = vertex.surfacePoint;

      SpectralPowerDistribution directSPD = new SpectralPowerDistribution();
      for (SpectralRadiatable radiatable : scene.SpectralRadiatables) {

         Point radiatableLocation = radiatable.getRandomPointOnSideOf(surfacePoint);

         Ray lightRayFromCurrentRadiatableToClosestDrawable = surfacePoint.CreateVectorFrom(radiatableLocation);

         lightRayFromCurrentRadiatableToClosestDrawable.OffsetOriginForward(.0001);

         // is it possible for the BRDF to reflect light from this incoming direction to our pregenerated outgoing direction?

         Vector pregeneratedOutgoing = Vector.Scale(vertex.outgoingDirection, -1);

         Vector possibleIncoming = lightRayFromCurrentRadiatableToClosestDrawable.Direction;

         double brdfPDF = vertex.surfaceBRDF.f(possibleIncoming, vertex.surfaceNormal, pregeneratedOutgoing);

         if (brdfPDF > 0) {
            // assuming some light would be reflected from this possible incoming, does the light shine anything in this direction?
            double lightPDF = radiatable.getPDF(surfacePoint, possibleIncoming);
            if (lightPDF > 0) {
               // assuming any light would hit, are there any occluders?

               IntersectionState potentialOccluder = scene.GetClosestDrawableToRay(lightRayFromCurrentRadiatableToClosestDrawable);

               boolean noOccluder = (potentialOccluder == null);
               boolean shadowRayHitLight = !noOccluder && potentialOccluder.Shape.equals(radiatable);

               if (shadowRayHitLight) {
                  potentialOccluder = scene.GetClosestDrawableToRayBeyond(lightRayFromCurrentRadiatableToClosestDrawable, potentialOccluder.TMin);
               }

               noOccluder = (potentialOccluder == null);
               boolean targetIntersection = !noOccluder && (potentialOccluder.Shape.equals(closestStateToRay.Shape) && Constants.WithinEpsilon(potentialOccluder.IntersectionPoint, closestStateToRay.IntersectionPoint));
               shadowRayHitLight = !noOccluder && potentialOccluder.Shape.equals(radiatable);

               if (shadowRayHitLight) {
                  // shit.. now what?
                  ;
               }

               if (noOccluder || targetIntersection) {
                  IntersectionState state = closestStateToRay.Shape.GetHitInfo(lightRayFromCurrentRadiatableToClosestDrawable);
                  if (state.Hits) {
                     // figure out how much light is shining by sampling the light

                     double pdfPercentage = brdfPDF * (4 * Math.PI) / lightPDF;
                     SpectralPowerDistribution currentIncomingSPD = radiatable.getSpectralPowerDistribution();
                     currentIncomingSPD = SpectralPowerDistribution.scale(currentIncomingSPD, pdfPercentage);
                     directSPD.add(currentIncomingSPD);
                  }
               }
            }
         }

      }
      return directSPD;
   }

   public ArrayList<LightVertex> getLightVertices() {

      ArrayList<LightVertex> lightVertices = new ArrayList<>();

      // TODO - fix this to properly select a light instead of just the first one
      SpectralRadiatable radiatable = scene.SpectralRadiatables.get(0);

      boolean proceed = (numFixedLightVertices == -1);

      if (proceed || numFixedLightVertices > 0) {

         /// get 0th vertex ///

         LightVertex lightVertex = null;

         // pick a random ray in the light's PDF
         Ray lightRay = radiatable.getRandomRayInPDF();
         SpectralSphereLight l = (SpectralSphereLight) radiatable;

         while (l.Inside(lightRay.Origin)) {
            // move the ray outside of the light, if it's inside
            lightRay.OffsetOriginForward(.5);
         }

         lightVertex = new LightVertex();
         lightVertex.density = 1;
         lightVertex.radiatable = radiatable;
         lightVertex.surfacePoint = lightRay.Origin;
         lightVertex.outgoingDirection = lightRay.Direction;
         lightVertex.outgoingSPD = l.getSpectralPowerDistribution();

         lightVertices.add(lightVertex);

         /// get 1th vertex ///

         if (proceed || numFixedLightVertices > 1) {

            lightVertex = WalkFirstLightPath(lightRay, radiatable);

            if (lightVertex != null) {

               lightVertices.add(lightVertex);

               /// subsequent vertices ///

               if (proceed || numFixedLightVertices > 2) {

                  int currentIteration = 2;

                  while (proceed || currentIteration < numFixedLightVertices) {
                     Ray nextRay = new Ray(lightVertex.surfacePoint, lightVertex.outgoingDirection);
                     lightVertex = RandomWalkLightPath(nextRay, lightVertex);
                     if (lightVertex == null)
                        break;
                     else {
                        lightVertices.add(lightVertex);

                     }
                     currentIteration++;
                  }
               }
            }
         }
      }
      return lightVertices;
   }



   private class LightVertex {
      /**
       * Pl / Pe.
       */
      public double density;


      /**
       * The direction of incoming light.
       */
      public Vector incomingDirection;

      /**
       * The incoming SPD.
       */
      public SpectralPowerDistribution incomingSPD;

      /**
       * PDF of the source of the incoming light (BRDF or light).
       */
      public double incomingPDF;
      public SpectralReflectanceCurve curve;
      public Point surfacePoint;
      public BRDF surfaceBRDF;
      public IntersectionState state;
      public Normal surfaceNormal;
      public double calculatedPDF;

      /**
       * The light that this point is on, if any.
       */
      public SpectralRadiatable radiatable;

      /**
       * The outgoing bounce direction calculated from the surface's BRDF.
       */
      public Vector outgoingDirection;

      /**
       * The SPD that would go towards the outgoingDirection.
       */
      public SpectralPowerDistribution outgoingSPD;
   }

   private class LightPassage  {
      public ArrayList<LightVertex> vertices;
      double p;
   }

   public LightVertex WalkFirstLightPath(Ray ray, SpectralRadiatable firstLight) {
      LightVertex l = new LightVertex();

      // does the ray hit anything?
      IntersectionState closestStateToRay = scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null) {
         // if not, we're done
         return null;
      }

      else if (closestStateToRay.Shape instanceof SpectralRadiatable) {
         // if we hit a light
         if (firstLight.equals(closestStateToRay.Shape)) {
            // we hit ourselves. shit.
            //System.out.println("Initial light ray hit same light source.");
            ray.Direction.Scale(-1);
            closestStateToRay = scene.GetClosestDrawableToRay(ray);

            if (closestStateToRay == null) {
               // if not, we're done
               //System.out.println("Initial light ray hit nothing after reversing.");
               return null;
            }

            else if (closestStateToRay.Shape instanceof SpectralRadiatable) {
               // if we hit a light
               if (firstLight.equals(closestStateToRay.Shape)) {
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
      Shape closestShape = closestStateToRay.Shape;
      Material objectMaterial = closestShape.GetMaterial();

      Normal intersectionNormal = closestStateToRay.Normal;
      Vector incomingDirection = ray.Direction;



      double pdf = firstLight.getPDF(closestStateToRay.IntersectionPoint, incomingDirection);
      double pdfPercentage = (4 * Math.PI) / pdf;

      SpectralPowerDistribution incomingSPD = firstLight.getSpectralPowerDistribution();
      //incomingSPD = SpectralPowerDistribution.scale(incomingSPD, pdfPercentage);

      SpectralReflectanceCurve curve = objectMaterial.SpectralReflectanceCurve;
      SpectralPowerDistribution reflectedSPD = incomingSPD.reflectOff(curve);
      Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection);

      double cosTheta = intersectionNormal.Dot(outgoingDirection);
      double incomingRadiantIntensityFactorForLambert = MonteCarloCalculations.CosineHemispherePDF(cosTheta, 0);
      reflectedSPD = SpectralPowerDistribution.scale(reflectedSPD, incomingRadiantIntensityFactorForLambert);

      l.calculatedPDF = pdfPercentage;
      l.incomingDirection = ray.Direction;
      l.incomingPDF = pdfPercentage;
      l.incomingSPD = incomingSPD;
      l.surfaceBRDF = objectMaterial.BRDF;
      l.curve = curve;
      l.surfaceNormal = intersectionNormal;
      l.surfacePoint = closestStateToRay.IntersectionPoint;
      l.outgoingDirection = outgoingDirection;
      l.outgoingSPD = reflectedSPD;

      return l;
   }

   public LightVertex RandomWalkLightPath(Ray ray, LightVertex previousVertex) {
      LightVertex l = new LightVertex();

      // does the ray hit anything?
      IntersectionState closestStateToRay = scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null || closestStateToRay.Shape instanceof SpectralRadiatable) {
         // if not, we're done
         return null;
      }
      /* TODO - fix this
            else if (closestStateToRay.Drawable instanceof SpectralRadiatable) {
               // if we hit a light
               //System.out.println("Subsequent light ray hit light source. Returning null.");
               return null;
            }
      */
      else {
         // we hit an actual object
         Shape closestShape = closestStateToRay.Shape;
         Material objectMaterial = closestShape.GetMaterial();

         Normal intersectionNormal = closestStateToRay.Normal;
         Vector incomingDirection = ray.Direction;

         BRDF brdf = objectMaterial.BRDF;
         Vector outgoingDirection = brdf.getVectorInPDF(intersectionNormal, incomingDirection);
         double calculatedPDF = brdf.f(incomingDirection, intersectionNormal, outgoingDirection);

         if (previousVertex.outgoingSPD == null) {
            int i = 0;
         }

         SpectralPowerDistribution incomingSPD = SpectralPowerDistribution.scale(previousVertex.outgoingSPD, 1);

         SpectralReflectanceCurve curve = objectMaterial.SpectralReflectanceCurve;
         SpectralPowerDistribution outgoingSPD = incomingSPD.reflectOff(curve);

         double cosTheta = intersectionNormal.Dot(outgoingDirection);
         double incomingRadiantIntensityFactorForLambert = MonteCarloCalculations.CosineHemispherePDF(cosTheta, 0);
         calculatedPDF *= incomingRadiantIntensityFactorForLambert;

         l.calculatedPDF = calculatedPDF;
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

   public ArrayList<LightVertex> getEyeVertices(Ray initialRay) {
      ArrayList<LightVertex> eyeVertices = new ArrayList<>();

      boolean proceed = (numFixedEyeVertices == -1);

      if (proceed || numFixedEyeVertices > 0) {

         /// get 0th vertex ///

         LightVertex eyeVertex = null;

         eyeVertex = new LightVertex();
         eyeVertex.calculatedPDF = 1;
         eyeVertex.surfacePoint = initialRay.Origin;
         eyeVertex.outgoingDirection = initialRay.Direction;

         eyeVertices.add(eyeVertex);

         /// get 1th vertex ///

         if (proceed || numFixedEyeVertices > 1) {

            eyeVertex = RandomWalkEyePath(initialRay);

            if (eyeVertex != null) {

               if (eyeVertex.curve == null) {
                  int foo = 2;
               }

               eyeVertex.calculatedPDF = 1;
               eyeVertices.add(eyeVertex);

               /// subsequent vertices ///

               if (proceed || numFixedEyeVertices > 2) {

                  int currentIteration = 2;

                  while (proceed || currentIteration < numFixedEyeVertices) {
                     Ray nextRay = new Ray(eyeVertex.surfacePoint, eyeVertex.incomingDirection);
                     eyeVertex = RandomWalkEyePath(nextRay);
                     if (eyeVertex == null)
                        break;
                     else {
                        eyeVertices.add(eyeVertex);

                     }
                     currentIteration++;
                  }
               }
            }
         }
      }
      //Collections.reverse(eyeVertices);

      return eyeVertices;
   }


   public LightVertex RandomWalkEyePath(Ray ray) {
      LightVertex l = new LightVertex();

      // does the ray hit anything?
      IntersectionState closestStateToRay = scene.GetClosestDrawableToRay(ray);

      if (closestStateToRay == null) {
         // if not, we're done
         return null;
      }

      else if (closestStateToRay.Shape instanceof SpectralRadiatable) {
         // if we hit a light
         //System.out.println("Subsequent light ray hit light source. Returning null.");
         return null;
      }
      else {
         // we hit an actual object
         Shape closestShape = closestStateToRay.Shape;
         Material objectMaterial = closestShape.GetMaterial();

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

         if (l.curve == null) {
            int i = 2;

         }

         return l;

      }
   }
}