package net.danielthompson.danray.integrators;

import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;


/**
 * Created by daniel on 5/5/15.
 */
public class PathTraceIntegrator extends AbstractIntegrator {

   public PathTraceIntegrator(AbstractScene scene, int maxDepth) {
      super(scene, maxDepth);
   }

   public Sample GetSample(Ray ray, int depth, int x, int y) {

      Sample sample = new Sample();
      IntersectionState closestStateToRay = scene.getNearestShape(ray, x, y);

      if (closestStateToRay == null || !closestStateToRay.Hits) {

         if (closestStateToRay != null) {
            sample.KDHeatCount = closestStateToRay.KDHeatCount;
            boolean xNan = Float.isNaN(closestStateToRay.IntersectionPoint.X);
            boolean yNan = Float.isNaN(closestStateToRay.IntersectionPoint.Y);
            boolean zNan = Float.isNaN(closestStateToRay.IntersectionPoint.Z);

            if (xNan || yNan || zNan) {
               IntersectionState closestStateToRay2 = scene.getNearestShape(ray, x, y);
               depth++;
               depth--;
            }

         }
         sample.SpectralPowerDistribution = scene.getSkyBoxSPD(ray.Direction);
         return sample;
      }

      boolean xNan = Float.isNaN(closestStateToRay.IntersectionPoint.X);
      boolean yNan = Float.isNaN(closestStateToRay.IntersectionPoint.Y);
      boolean zNan = Float.isNaN(closestStateToRay.IntersectionPoint.Z);

      if (xNan || yNan || zNan) {
         IntersectionState closestStateToRay2 = scene.getNearestShape(ray, x, y);
         depth++;
         depth--;
      }

      if (closestStateToRay.Shape instanceof AbstractLight) {
         sample.SpectralPowerDistribution = ((AbstractLight) closestStateToRay.Shape).SpectralPowerDistribution;
         return sample;
      }

      // base case
      if (depth >= maxDepth) {
         sample.SpectralPowerDistribution = new SpectralPowerDistribution();
         return sample;
      }
      else {
         AbstractShape closestShape = closestStateToRay.Shape;
         if (closestShape == null) {
            sample.SpectralPowerDistribution = new SpectralPowerDistribution();
            return sample;
         }
         Material objectMaterial = closestShape.Material;

         Normal intersectionNormal = closestStateToRay.Normal;
         Vector incomingDirection = ray.Direction;
//
//         if (x == 476 && y == 531) {
//            int j = 0;
//            Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection);
//         }

         Vector outgoingDirection = objectMaterial.BRDF.getVectorInPDF(intersectionNormal, incomingDirection);
         float scalePercentage = objectMaterial.BRDF.f(incomingDirection, intersectionNormal, outgoingDirection);

         Ray bounceRay = new Ray(closestStateToRay.IntersectionPoint, outgoingDirection);

         bounceRay.OffsetOriginForward(Constants.HalfEpsilon);

         Sample incomingSample = GetSample(bounceRay, depth + 1, x, y);

         SpectralPowerDistribution incomingSPD = incomingSample.SpectralPowerDistribution;

         incomingSPD = SpectralPowerDistribution.scale(incomingSPD, scalePercentage);

         // compute the interaction of the incoming SPD with the object's SRC

         ReflectanceSpectrum reflectanceSpectrum = objectMaterial.ReflectanceSpectrum;

         SpectralPowerDistribution reflectedSPD = incomingSPD.reflectOff(reflectanceSpectrum);

         sample.SpectralPowerDistribution = reflectedSPD;

         return sample;
      }

   }

}
