package net.danielthompson.danray.shading.bxdf;

import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector;
import net.danielthompson.danray.utility.MonteCarloCalculations;

/**
 * Created by dthompson on 21 May 15.
 */
public abstract class BxDF {

   public Vector WorldToLocal(Intersection intersection, Vector v) {
      return new Vector(
            v.Dot(intersection.TangentU),
            v.Dot(intersection.Normal),
            v.Dot(intersection.TangentV)
      );
   }

   public Normal WorldToLocal(Intersection intersection, Normal n) {
      return new Normal(
            n.Dot(intersection.TangentU),
            n.Dot(intersection.Normal),
            n.Dot(intersection.TangentV)
      );
   }

   public void LocalToWorldInPlace(Intersection intersection, Vector v) {
      float x = intersection.TangentU.X * v.X + intersection.Normal.X * v.Y + intersection.TangentV.X * v.Z;
      float y = intersection.TangentU.Y * v.X + intersection.Normal.Y * v.Y + intersection.TangentV.Y * v.Z;
      float z = intersection.TangentU.Z * v.X + intersection.Normal.Z * v.Y + intersection.TangentV.Z * v.Z;
      v.X = x;
      v.Y = y;
      v.Z = z;
   }

   public Vector LocalToWorld(Intersection intersection, Vector v) {
      return new Vector(
            intersection.TangentU.X * v.X + intersection.Normal.X * v.Y + intersection.TangentV.X * v.Z,
            intersection.TangentU.Y * v.X + intersection.Normal.Y * v.Y + intersection.TangentV.Y * v.Z,
            intersection.TangentU.Z * v.X + intersection.Normal.Z * v.Y + intersection.TangentV.Z * v.Z
      );
   }

   public Normal LocalToWorld(Intersection intersection, Normal n) {
      return new Normal(
            intersection.TangentU.X * n.X + intersection.Normal.X * n.Y + intersection.TangentV.X * n.Z,
            intersection.TangentU.Y * n.X + intersection.Normal.Y * n.Y + intersection.TangentV.Y * n.Z,
            intersection.TangentU.Z * n.X + intersection.Normal.Z * n.Y + intersection.TangentV.Z * n.Z
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
   public abstract Vector getVectorInPDF(Normal normal, Vector incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction);

   public Vector getVectorInPDF(Normal normal, Vector incoming) {
      return getVectorInPDF(normal, incoming, 1, 1);
   }

   public Vector getVectorInPDF(Intersection intersection, Vector incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {
      // local space
      Vector outgoing = MonteCarloCalculations.CosineSampleHemisphere();

      // world space
      LocalToWorldInPlace(intersection, outgoing);
      return outgoing;
   }

   public Vector getVectorInPDF(Intersection intersection, Vector incoming) {
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
   public abstract float f(Vector incoming, Normal normal, Vector outgoing);

}
