package net.danielthompson.danray.shading.bxdf;

import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector;

/**
 * Created by dthompson on 21 May 15.
 */
public class GaussianBRDF extends BRDF {

   public double Sigma;

   public GaussianBRDF(double sigma) {

      Sigma = sigma;
   }

   @Override
   public double f(double thetaIncoming, double thetaOutgoing) {

      return super.f(thetaIncoming, thetaOutgoing);
   }

   @Override
   public Vector getVectorInPDF(Normal normal, Vector outgoing) {
      return null;
   }

   @Override
   public double f(Vector incoming, Normal normal, Vector outgoing) {
      return 0;
   }
}
