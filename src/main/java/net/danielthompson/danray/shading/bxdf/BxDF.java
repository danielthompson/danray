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
            v.dot(intersection.TangentU),
            v.dot(intersection.Normal),
            v.dot(intersection.TangentV)
      );
   }

   public Normal WorldToLocal(Intersection intersection, Normal n) {
      return new Normal(
            n.Dot(intersection.TangentU),
            n.Dot(intersection.Normal),
            n.Dot(intersection.TangentV)
      );
   }

   public void LocalToWorldInPlace(Intersection intersection, Vector3 v) {
      float x = intersection.TangentU.x * v.x + intersection.Normal.x * v.y + intersection.TangentV.x * v.z;
      float y = intersection.TangentU.y * v.x + intersection.Normal.y * v.y + intersection.TangentV.y * v.z;
      float z = intersection.TangentU.z * v.x + intersection.Normal.z * v.y + intersection.TangentV.z * v.z;
      v.x = x;
      v.y = y;
      v.z = z;
   }

   public Vector3 LocalToWorld(Intersection intersection, Vector3 v) {
      return new Vector3(
            intersection.TangentU.x * v.x + intersection.Normal.x * v.y + intersection.TangentV.x * v.z,
            intersection.TangentU.y * v.x + intersection.Normal.y * v.y + intersection.TangentV.y * v.z,
            intersection.TangentU.z * v.x + intersection.Normal.z * v.y + intersection.TangentV.z * v.z
      );
   }

   public Normal LocalToWorld(Intersection intersection, Normal n) {
      return new Normal(
            intersection.TangentU.x * n.x + intersection.Normal.x * n.y + intersection.TangentV.x * n.z,
            intersection.TangentU.y * n.x + intersection.Normal.y * n.y + intersection.TangentV.y * n.z,
            intersection.TangentU.z * n.x + intersection.Normal.z * n.y + intersection.TangentV.z * n.z
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
