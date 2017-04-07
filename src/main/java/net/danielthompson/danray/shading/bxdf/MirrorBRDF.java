package net.danielthompson.danray.shading.bxdf;

import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector;
import net.danielthompson.danray.utility.GeometryCalculations;

/**
 * Created by dthompson on 21 May 15.
 */
public class MirrorBRDF extends BRDF {

   private Vector ZeroVector = new Vector(0,0,0);

   public MirrorBRDF() {
      Delta = true;
   }

   @Override
   public float f(float thetaIncoming, float thetaOutgoing) {
      if (Constants.WithinEpsilon(thetaIncoming, thetaOutgoing))
         return 1.0f;
      return 0.0f;
   }

   @Override
   public float f(Vector incoming, Normal normal, Vector outgoing) {

      Vector fixedIncoming = Vector.Scale(incoming, -1);
      float thetaIncoming = GeometryCalculations.radiansBetween(fixedIncoming, normal);
      float thetaOutgoing = GeometryCalculations.radiansBetween(outgoing, normal);

      return f(thetaIncoming, thetaOutgoing);

   }

   @Override
   public Vector getVectorInPDF(Normal normal, Vector incoming) {
      normal.Normalize();
      float factor = incoming.Dot(normal) * 2;
      Vector scaled = new Vector(Normal.Scale(normal, factor));

      //Vector outgoing = Vector.Minus(incoming, scaled);

      Vector outgoing = Vector.Minus(ZeroVector, Vector.Minus(scaled, incoming));

      return outgoing;

   }
}
