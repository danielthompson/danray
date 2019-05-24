package net.danielthompson.danray.shading.bxdf;

import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector3;
import net.danielthompson.danray.utility.MonteCarloCalculations;

/**
 * Created by dthompson on 21 May 15.
 */
public abstract class BxDF {

   public Vector3 WorldToLocal(Intersection intersection, Vector3 v) {
      return new Vector3(
            v.dot(intersection.tangentU),
            v.dot(intersection.normal),
            v.dot(intersection.tangentV)
      );
   }

   public Normal WorldToLocal(Intersection intersection, Normal n) {
      return new Normal(
            n.dot(intersection.tangentU),
            n.dot(intersection.normal),
            n.dot(intersection.tangentV)
      );
   }

   public void LocalToWorldInPlace(Intersection intersection, Vector3 v) {
      float x = intersection.tangentU.x * v.x + intersection.normal.x * v.y + intersection.tangentV.x * v.z;
      float y = intersection.tangentU.y * v.x + intersection.normal.y * v.y + intersection.tangentV.y * v.z;
      float z = intersection.tangentU.z * v.x + intersection.normal.z * v.y + intersection.tangentV.z * v.z;
      v.x = x;
      v.y = y;
      v.z = z;
   }

   public Vector3 LocalToWorld(Intersection intersection, Vector3 v) {
      return new Vector3(
            intersection.tangentU.x * v.x + intersection.normal.x * v.y + intersection.tangentV.x * v.z,
            intersection.tangentU.y * v.x + intersection.normal.y * v.y + intersection.tangentV.y * v.z,
            intersection.tangentU.z * v.x + intersection.normal.z * v.y + intersection.tangentV.z * v.z
      );
   }

   public Normal LocalToWorld(Intersection intersection, Normal n) {
      return new Normal(
            intersection.tangentU.x * n.x + intersection.normal.x * n.y + intersection.tangentV.x * n.z,
            intersection.tangentU.y * n.x + intersection.normal.y * n.y + intersection.tangentV.y * n.z,
            intersection.tangentU.z * n.x + intersection.normal.z * n.y + intersection.tangentV.z * n.z
      );
   }

   /**
    *
    * @param thetaIncoming
    * @param thetaOutgoing
    * @return
    */
   public float f(float thetaIncoming, float thetaOutgoing) {
      return 0.0f;
   }

   /**
    * Whether or not the distribution is a delta distribution.
    * If so, it should be sampled with f_delta(), not f().
    */
   public boolean Delta;

   public boolean Glossy;

   public boolean Transmission = false;

   /**
    * Gets an outgoing vector according to the BxDF's PDF.
    * Should be used for both delta and non-delta.
    * @param normal The surface normal at the point of intersection.
    * @param incoming The incoming vector.
    * @return A vector randomly sampled according to the BxDF's PDF.
    */
   public abstract Vector3 getVectorInPDF(Normal normal, Vector3 incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction);

   public Vector3 getVectorInPDF(Normal normal, Vector3 incoming) {
      return getVectorInPDF(normal, incoming, 1, 1);
   }

   public Vector3 getVectorInPDF(Intersection intersection, Vector3 incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {
      // local space
      Vector3 outgoing = MonteCarloCalculations.CosineSampleHemisphere();

      // world space
      LocalToWorldInPlace(intersection, outgoing);
      return outgoing;
   }

   public Vector3 getVectorInPDF(Intersection intersection, Vector3 incoming) {
      return getVectorInPDF(intersection, incoming, 1, 1);
   }

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
   public abstract float f(Vector3 incoming, Normal normal, Vector3 outgoing);

}
