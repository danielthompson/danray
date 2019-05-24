package net.danielthompson.danray.shading.bxdf.transmit;

import net.danielthompson.danray.shading.bxdf.BTDF;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector3;
import net.danielthompson.danray.utility.GeometryCalculations;

/**
 * Created by daniel on 8/16/15.
 */
public class LambertianBTDF extends BTDF {

   @Override
   public float f(float thetaIncoming, float thetaOutgoing) {
      return Constants.OneOverPi;
   }

   @Override
   public float f(Vector3 incoming, Normal normal, Vector3 outgoing) {
         return Constants.OneOverPi;
   }

   @Override
   public Vector3 getVectorInPDF(Normal normal, Vector3 incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {
      Vector3 outgoing = GeometryCalculations.randomVectorOnSphere();

      if (outgoing.dot(normal) > 0)
         outgoing.scale(-1);

      return outgoing;
   }
}