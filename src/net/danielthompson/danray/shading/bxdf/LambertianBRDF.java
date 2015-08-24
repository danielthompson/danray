package net.danielthompson.danray.shading.bxdf;

import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.tracers.GeometryCalculations;

/**
 * Created by daniel on 8/16/15.
 */
public class LambertianBRDF extends BRDF {

   @Override
   public double f(double thetaIncoming, double thetaOutgoing) {
      return Constants.OneOver2Pi;
   }

   @Override
   public double f(Vector incoming, Normal normal, Vector outgoing) {
      return Constants.OneOver2Pi;
   }

   @Override
   public Vector getVectorInPDF(Normal normal, Vector incoming) {
      double[] xyz = GeometryCalculations.randomPointOnSphere(GeometryCalculations.Random);

      Vector outgoing = new Vector(xyz[0], xyz[1], xyz[2]);

      if (outgoing.Dot(normal) < 0)
         outgoing.Scale(-1);

      return outgoing;
   }
}