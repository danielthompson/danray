package net.danielthompson.danray.shading.bxdf.reflect;

import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector;
import net.danielthompson.danray.utility.GeometryCalculations;
import net.danielthompson.danray.utility.MonteCarloCalculations;

/**
 * Created by dthompson on 21 May 15.
 */
public class SpecularBRDF extends BRDF {

   private Vector ZeroVector = new Vector(0,0,0);

   public SpecularBRDF() {
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
   public Vector getVectorInPDF(Intersection intersection, Vector incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {
      return getVectorInPDF(intersection.Normal, incoming, leavingIndexOfRefraction, enteringIndexOfRefraction);
   }

   @Override
   public Vector getVectorInPDF(Normal normal, Vector incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {
      normal.Normalize();
      float factor = incoming.Dot(normal) * 2;
      Vector scaled = new Vector(Normal.Scale(normal, factor));

      //Vector outgoing = Vector.Minus(incoming, scaled);

      Vector outgoing = Vector.Minus(scaled, incoming);

      outgoing = Vector.Minus(ZeroVector, outgoing);

      return outgoing;
   }
}
