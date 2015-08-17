package net.danielthompson.danray.shading.bxdf;

import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector;
import net.danielthompson.danray.tracers.GeometryCalculations;

/**
 * Created by dthompson on 21 May 15.
 */
public class MirrorBRDF extends BRDF {

   @Override
   public double f(double thetaIncoming, double thetaOutgoing) {
      if (Constants.WithinDelta(thetaIncoming, thetaOutgoing))
         return 1.0;
      return 0.0;
   }

   @Override
   public Vector getVectorInPDF(Normal normal, Vector incoming) {
      normal.Normalize();
      double factor = incoming.Dot(normal) * 2;
      Vector scaled = new Vector(Normal.Scale(normal, factor));
      Vector outgoing = Vector.Minus(new Vector(0, 0, 0), Vector.Minus(scaled, incoming));

      return outgoing;

   }
}
