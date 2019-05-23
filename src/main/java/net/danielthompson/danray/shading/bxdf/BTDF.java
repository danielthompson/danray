package net.danielthompson.danray.shading.bxdf;

import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Vector3;

public abstract class BTDF extends BxDF {
   protected BTDF() {
      Transmission = true;
   }

   @Override
   public Vector3 getVectorInPDF(Intersection intersection, Vector3 incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {
      return getVectorInPDF(intersection.normal, incoming, leavingIndexOfRefraction, enteringIndexOfRefraction);
   }

}
