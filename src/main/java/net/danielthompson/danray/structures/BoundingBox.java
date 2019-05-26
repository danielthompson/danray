package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.utility.FloatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: daniel
 * Date: 7/21/13
 * Time: 15:44
 */
public class BoundingBox {
   /**
    * "Lower" point. Always in world-space.
    */
   public Point3 point1;

   /**
    * "Upper" point. Always in world space.
    */
   public Point3 point2;

   public BoundingBox (final Point3 p0, final Point3 p1) {
      this.point1 = new Point3(p0);
      this.point2 = new Point3(p1);

      if (this.point2.x < this.point1.x) {
         final float temp = this.point2.x;
         this.point2.x = this.point1.x;
         this.point1.x = temp;
      }

      if (this.point2.y < this.point1.y) {
         final float temp = this.point2.y;
         this.point2.y = this.point1.y;
         this.point1.y = temp;
      }

      if (this.point2.z < this.point1.z) {
         final float temp = this.point2.z;
         this.point2.z = this.point1.z;
         this.point1.z = temp;
      }

      assert this.point1.x <= this.point2.x;
      assert this.point1.y <= this.point2.y;
      assert this.point1.z <= this.point2.z;
   }

   public BoundingBox(final BoundingBox b) {
      this.point1 = new Point3(b.point1);
      this.point2 = new Point3(b.point2);

      if (point2.x < point1.x) {
         final float temp = point2.x;
         point2.x = point1.x;
         point1.x = temp;
      }

      if (point2.y < point1.y) {
         final float temp = point2.y;
         point2.y = point1.y;
         point1.y = temp;
      }

      if (point2.z < point1.z) {
         final float temp = point2.z;
         point2.z = point1.z;
         point1.z = temp;
      }

      assert point1.x <= point2.x;
      assert point1.y <= point2.y;
      assert point1.z <= point2.z;
   }

   public float getUpperBoundInAxis(final KDAxis axis) {
      return Math.max(point1.getAxis(axis), point2.getAxis(axis));
   }

   public float getLowerBoundInAxis(final KDAxis axis) {
      return Math.min(point1.getAxis(axis), point2.getAxis(axis));
   }

   public float getVolume() {
      return Math.abs((point2.x - point1.x) * (point2.y - point1.y) * (point2.z - point1.z));
   }

   public float getSurfaceArea() {
      final float xLength = Math.abs(point2.x - point1.x);
      final float yLength = Math.abs(point2.y - point1.y);
      final float zLength = Math.abs(point2.z - point1.z);

      final float surfaceArea = 2 * (xLength * yLength + xLength * zLength + yLength * zLength);

      return surfaceArea;

   }

   public KDAxis getLargestExtent() {
      final float x = point2.x - point1.x;
      final float y = point2.y - point1.y;
      final float z = point2.z - point1.z;

      if (x >= y) {
         if (x >= z)
            return KDAxis.X;
         else
            return KDAxis.Z;
      }
      else {
         if (y >= z) {
            return KDAxis.Y;
         }
         else
            return KDAxis.Z;
      }
   }

   public float getMedian(final KDAxis axis) {
      final float median = (point1.getAxis(axis) + point2.getAxis(axis)) * 0.5f;
      return median;
   }

   public Intersection getHitInfo(final Ray ray) {
      return BoundingBox.getHitInfo(point1, point2, ray);
   }

   public static Intersection getHitInfo(final Point3 p0, final Point3 p1, final Ray r) {
      float maxBoundFarT = Float.MAX_VALUE;
      float minBoundNearT = 0;

      final Intersection intersection = new Intersection();
      intersection.hits = true;

      // x
      float tNear = (p0.x - r.Origin.x) * r.DirectionInverse.x;
      float tFar = (p1.x - r.Origin.x) * r.DirectionInverse.x;

      float swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      tFar *= 1 + 2 * Constants.Gamma3;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         intersection.hits = false;
         return intersection;
      }

      intersection.t = minBoundNearT;
      //intersection.TMax = maxBoundFarT;

      // y
      tNear = (p0.y - r.Origin.y) * r.DirectionInverse.y;
      tFar = (p1.y - r.Origin.y) * r.DirectionInverse.y;

      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      tFar *= 1 + 2 * Constants.Gamma3;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         intersection.hits = false;
         return intersection;
      }

      intersection.t = minBoundNearT;
      //intersection.TMax = maxBoundFarT;

      // z
      tNear = (p0.z - r.Origin.z) * r.DirectionInverse.z;
      tFar = (p1.z - r.Origin.z) * r.DirectionInverse.z;

      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      tFar *= 1 + 2 * Constants.Gamma3;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         intersection.hits = false;
         return intersection;
      }

      intersection.t = minBoundNearT;
      //intersection.TMax = maxBoundFarT;

      return intersection;
   }

   public static List<Intersection> getBothHitInfo(final Point3 p0, final Point3 p1, final Ray r) {
      float maxBoundFarT = Float.MAX_VALUE;
      float minBoundNearT = 0;

      final List<Intersection> intersections = new ArrayList<>();

      // x
      float tNear = (p0.x - r.Origin.x) * r.DirectionInverse.x;
      float tFar = (p1.x - r.Origin.x) * r.DirectionInverse.x;

      float swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         return intersections;
      }

      // y
      tNear = (p0.y - r.Origin.y) * r.DirectionInverse.y;
      tFar = (p1.y - r.Origin.y) * r.DirectionInverse.y;

      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         return intersections;
      }

      // z
      tNear = (p0.z - r.Origin.z) * r.DirectionInverse.z;
      tFar = (p1.z - r.Origin.z) * r.DirectionInverse.z;

      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         return intersections;
      }

      if (minBoundNearT > 0) {
         final Intersection intersection = new Intersection();
         intersection.t = minBoundNearT;
         intersection.hits = true;
         intersections.add(intersection);
      }

      final Intersection intersection = new Intersection();
      intersection.t = maxBoundFarT;
      intersection.hits = true;
      intersections.add(intersection);

      return intersections;
   }

   public boolean hits(final Ray r) {
      return (r.Origin.x >= point1.x && r.Origin.x <= point2.x
            && r.Origin.y >= point1.y && r.Origin.y <= point2.y
            && r.Origin.z >= point1.z && r.Origin.z <= point2.z) || BoundingBox.getHitInfo(point1, point2, r).hits;
   }

   public void translate(final Vector3 v) {
      point1.plus(v);
      point2.plus(v);
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof BoundingBox))
         return false;

      final BoundingBox rhs = (BoundingBox) obj;
      return (point1.equals(rhs.point1) && point2.equals(rhs.point2));
   }

   public static void expand(final BoundingBox b0, final BoundingBox b1) {
      b0.point1.x = Math.min(b0.point1.x, b1.point1.x);
      b0.point1.y = Math.min(b0.point1.y, b1.point1.y);
      b0.point1.z = Math.min(b0.point1.z, b1.point1.z);

      b0.point2.x = Math.max(b0.point2.x, b1.point2.x);
      b0.point2.y = Math.max(b0.point2.y, b1.point2.y);
      b0.point2.z = Math.max(b0.point2.z, b1.point2.z);
   }

   public static BoundingBox union(final BoundingBox b0, final BoundingBox b1) {
      final float p0x = Math.min(b0.point1.x, b1.point1.x);
      final float p0y = Math.min(b0.point1.y, b1.point1.y);
      final float p0z = Math.min(b0.point1.z, b1.point1.z);
      final Point3 p0 = new Point3(p0x, p0y, p0z);

      final float p1x = Math.max(b0.point2.x, b1.point2.x);
      final float p1y = Math.max(b0.point2.y, b1.point2.y);
      final float p1z = Math.max(b0.point2.z, b1.point2.z);
      final Point3 p1 = new Point3(p1x, p1y, p1z);

      final BoundingBox box = new BoundingBox(p0, p1);
      return box;
   }

   public static BoundingBox difference(final BoundingBox b0, final BoundingBox b1) {
      return new BoundingBox(b0);
   }

   public static BoundingBox intersection(final BoundingBox b0, final BoundingBox b1) {
      return union(b0, b1);
   }

   /**
    * Checks to see if b1 is strictly inside of b2.
    * @param b0
    * @param b1
    * @return
    */
   public static boolean inside(final BoundingBox b0, final BoundingBox b1) {
      return (b0.point1.x > b1.point1.x
            && b0.point1.y > b1.point1.y
            && b0.point1.z > b1.point1.z
            && b0.point2.x < b1.point2.x
            && b0.point2.y < b1.point2.y
            && b0.point2.z < b1.point2.z);
   }

   public static BoundingBox GetBoundingBox(final BoundingBox b, final Point3 p) {
      final float p0x = Math.min(b.point1.x, p.x);
      final float p0y = Math.min(b.point1.y, p.y);
      final float p0z = Math.min(b.point1.z, p.z);
      final Point3 p0 = new Point3(p0x, p0y, p0z);

      final float p1x = Math.max(b.point2.x, p.x);
      final float p1y = Math.max(b.point2.y, p.y);
      final float p1z = Math.max(b.point2.z, p.z);
      final Point3 p1 = new Point3(p1x, p1y, p1z);

      return new BoundingBox(p0, p1);
   }
}
