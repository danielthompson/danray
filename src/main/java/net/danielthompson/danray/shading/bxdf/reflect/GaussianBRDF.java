package net.danielthompson.danray.shading.bxdf.reflect;

import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector3;

/**
 * Created by dthompson on 21 May 15.
 */
public class GaussianBRDF extends BRDF {

   public float Sigma;

   public GaussianBRDF(float sigma) {

      Sigma = sigma;
   }

   @Override
   public float f(float thetaIncoming, float thetaOutgoing) {

      return super.f(thetaIncoming, thetaOutgoing);
   }

   @Override
   public Vector3 getVectorInPDF(Normal normal, Vector3 incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {
      return null;
   }

   @Override
   public float f(Vector3 incoming, Normal normal, Vector3 outgoing) {
      return 0;
   }
}
