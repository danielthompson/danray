package net.danielthompson.danray.shading.bxdf.reflect;

import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector3;
import net.danielthompson.danray.utility.GeometryCalculations;

/**
 * Created by dthompson on 21 May 15.
 */
public class SpecularBRDF extends BRDF {

   private Vector3 ZeroVector = new Vector3(0,0,0);

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
   public float f(Vector3 incoming, Normal normal, Vector3 outgoing) {

      Vector3 fixedIncoming = Vector3.scale(incoming, -1);
      float thetaIncoming = GeometryCalculations.radiansBetween(fixedIncoming, normal);
      float thetaOutgoing = GeometryCalculations.radiansBetween(outgoing, normal);

      return f(thetaIncoming, thetaOutgoing);

   }

   @Override
   public Vector3 getVectorInPDF(Intersection intersection, Vector3 incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {
      return getVectorInPDF(intersection.normal, incoming, leavingIndexOfRefraction, enteringIndexOfRefraction);
   }

   @Override
   public Vector3 getVectorInPDF(Normal normal, Vector3 incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {
      normal.normalize();
      float factor = incoming.dot(normal) * 2;
      Vector3 scaled = new Vector3(Normal.scale(normal, factor));

      //Vector outgoing = Vector.minus(incoming, scaled);

      Vector3 outgoing = Vector3.minus(scaled, incoming);

      outgoing = Vector3.minus(ZeroVector, outgoing);

      return outgoing;
   }
}
