package net.danielthompson.danray.shading.bxdf;

import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.utility.GeometryCalculations;

/**
 * Created by daniel on 8/16/15.
 */
public class LambertianBRDF extends BRDF {

   @Override
   public double f(double thetaIncoming, double thetaOutgoing) {
      return Constants.OneOverPi;
   }

   @Override
   public double f(Vector incoming, Normal normal, Vector outgoing) {

      
      //if (incoming.Dot(normal) <= 0 && normal.Dot(outgoing) >= 0)
         return Constants.OneOverPi;
      //return 0;
   }

   @Override
   public Vector getVectorInPDF(Normal normal, Vector incoming) {
      double[] xyz = GeometryCalculations.randomPointOnPregeneratedSphere();

      Vector outgoing = new Vector(xyz[0], xyz[1], xyz[2]);

      if (outgoing.Dot(normal) < 0)
         outgoing.Scale(-1);

      return outgoing;
   }
}