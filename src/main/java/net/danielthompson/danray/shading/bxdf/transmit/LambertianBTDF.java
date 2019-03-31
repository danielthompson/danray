package net.danielthompson.danray.shading.bxdf.transmit;

import net.danielthompson.danray.shading.bxdf.BTDF;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector;
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
   public float f(Vector incoming, Normal normal, Vector outgoing) {
         return Constants.OneOverPi;
   }

   @Override
   public Vector getVectorInPDF(Normal normal, Vector incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {
      float[] xyz = GeometryCalculations.randomPointOnSphere();
      Vector outgoing = new Vector(xyz[0], xyz[1], xyz[2]);

      if (outgoing.Dot(normal) > 0)
         outgoing.Scale(-1);

      return outgoing;
   }
}