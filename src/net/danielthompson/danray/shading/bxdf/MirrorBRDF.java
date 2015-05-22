package net.danielthompson.danray.shading.bxdf;

/**
 * Created by dthompson on 21 May 15.
 */
public class MirrorBRDF extends BRDF {

   public double Gloss;

   public MirrorBRDF(double gloss) {
      Gloss = gloss;
   }

   @Override
   public double f(double thetaIncoming, double thetaOutgoing) {
      if (thetaIncoming == thetaOutgoing)
         return 1.0;
      return 0.0;
   }
}
