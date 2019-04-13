package net.danielthompson.danray.shading.bxdf.reflect;

import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.utility.GeometryCalculations;

/**
 * Created by daniel on 8/16/15.
 */
public class LambertianBRDF extends BRDF {

   @Override
   public float f(float thetaIncoming, float thetaOutgoing) {
      return 1;
   }

   @Override
   public float f(Vector incoming, Normal normal, Vector outgoing) {
         return 1;
   }

   @Override
   public Vector getVectorInPDF(Normal normal, Vector incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {
      float[] xyz = GeometryCalculations.randomPointOnSphere();
      Vector outgoing = new Vector(xyz[0], xyz[1], xyz[2]);

      if (outgoing.Dot(normal) < 0)
         outgoing.Scale(-1);

      return outgoing;
   }

}