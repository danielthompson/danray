package net.danielthompson.danray.shading.bxdf;

import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector;

/**
 * Created by dthompson on 21 May 15.
 */
public abstract class BxDF {
   public double f(double thetaIncoming, double thetaOutgoing) {
      return 0.0;
   }

   /**
    * Whether or not the distribution is a delta distribution.
    * If so, it should be sampled with f_delta(), not f().
    */
   boolean Delta;

   /**
    * Gets an outgoing vector according to the BxDF's PDF.
    * Should be used for both delta and non-delta.
    * @param normal The surface normal at the point of intersection.
    * @param incoming The incoming vector.
    * @return A vector randomly sampled according to the BxDF's PDF.
    */
   public abstract Vector getVectorInPDF(Normal normal, Vector incoming);

   /**
    * Returns the proportion of outgoing light that comes from the incoming direction.
    * Should only be used for non-delta distributions - f() can be assumed to be 0 for deltas
    * except when the outgoing Vector has been obtained from getVectorInPDF(), in which case
    * there should be no need to call this since it can be assumed to be 1.
    * @param incoming The direction of incoming light.
    * @param normal The surface normal at the point of intersection.
    * @param outgoing The direction of outgoing light.
    * @return The proportion of outgoing light that comes from the incoming direction.
    */
   public abstract double f(Vector incoming, Normal normal, Vector outgoing);

}
