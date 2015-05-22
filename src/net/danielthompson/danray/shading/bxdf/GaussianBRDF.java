package net.danielthompson.danray.shading.bxdf;

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
}
