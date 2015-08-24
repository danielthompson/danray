package net.danielthompson.danray.shading.bxdf;

import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector;
import net.danielthompson.danray.tracers.GeometryCalculations;

/**
 * Created by dthompson on 21 May 15.
 */
public abstract class BxDF {
   public double f(double thetaIncoming, double thetaOutgoing) {
      return 0.0;
   }

   public abstract Vector getVectorInPDF(Normal normal, Vector incoming);

   public abstract double f(Vector incoming, Normal normal, Vector outgoing);
}
