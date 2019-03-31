package net.danielthompson.danray.shading.bxdf;

import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Vector;

public abstract class BTDF extends BxDF {
   protected BTDF() {
      Transmission = true;
   }

   @Override
   public Vector getVectorInPDF(Intersection intersection, Vector incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {
      return getVectorInPDF(intersection.Normal, incoming, leavingIndexOfRefraction, enteringIndexOfRefraction);
   }

}
