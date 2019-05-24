package net.danielthompson.danray.shading.bxdf.reflect;

import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Vector3;
import net.danielthompson.danray.utility.GeometryCalculations;

/**
 * Created by daniel on 8/16/15.
 */
public class FlatBRDF extends BRDF {

   @Override
   public float f(float thetaIncoming, float thetaOutgoing) {

      return Constants.OneOverPi;
   }

   @Override
   public float f(Vector3 incoming, Normal normal, Vector3 outgoing) {

      if (incoming.dot(normal) <= 0 && normal.Dot(outgoing) >= 0)
         return Constants.OneOverPi;
      return 0;
   }

   @Override
   public Vector3 getVectorInPDF(Normal normal, Vector3 incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {

      Vector3 outgoing = GeometryCalculations.randomVectorOnSphere();

      if (outgoing.dot(normal) < 0)
         outgoing.scale(-1);

      return outgoing;
   }
}