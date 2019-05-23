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
   public float f(Vector3 incoming, Normal normal, Vector3 outgoing) {
         return 1;
   }

   @Override
   public Vector3 getVectorInPDF(Normal normal, Vector3 incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {
//      if (normal.dot(incoming) > 0)
//         normal.scale(-1);

      float[] xyz = GeometryCalculations.randomPointOnSphere();
      Vector3 outgoing = new Vector3(xyz[0], xyz[1], xyz[2]);

      if (outgoing.dot(normal) < 0)
         outgoing.scale(-1);

      return outgoing;
   }

}