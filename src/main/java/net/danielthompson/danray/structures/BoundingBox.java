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

   public BoundingBox (Point3 point1, Point3 point2) {
      this.point1 = point1.clone();
      this.point2 = point2.clone();
   }

   public BoundingBox(BoundingBox box) {
      this.point1 = box.point1.clone();
      this.point2 = box.point2.clone();
   }

   public float getUpperBoundInAxis(KDAxis axis) {
      return Math.max(point1.getAxis(axis), point2.getAxis(axis));
   }

   public float getLowerBoundInAxis(KDAxis axis) {
      return Math.min(point1.getAxis(axis), point2.getAxis(axis));
   }

   public float GetVolume() {
      return Math.abs((point2.x - point1.x) * (point2.y - point1.y) * (point2.z - point1.z));
   }

   public float getSurfaceArea() {
      float xLength = Math.abs(point2.x - point1.x);
      float yLength = Math.abs(point2.y - point1.y);
      float zLength = Math.abs(point2.z - point1.z);

      float surfaceArea = 2 * (xLength * yLength + xLength * zLength + yLength * zLength);

      return surfaceArea;

   }

   public KDAxis getLargestExtent() {
      float x = point2.x - point1.x;
      float y = point2.y - point1.y;
      float z = point2.z - point1.z;

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


   public BoundingBox GetWorldBoundingBox() {
      return this;
   }

   public float getMedian(KDAxis axis) {
      float median = (point1.getAxis(axis) + point2.getAxis(axis)) / 2.0f;
      return median;
   }

   public Intersection GetHitInfo(Ray ray) {
      return BoundingBox.GetHitInfoNew(point1, point2, ray);
   }

   // orig
   public static Intersection GetHitInfoOld(Point3 p1, Point3 p2, Ray ray) {
      float maxBoundFarT = Float.MAX_VALUE;
      float minBoundNearT = 0;

      Intersection state = new Intersection();
      state.Hits = true;

      // x
      float tNear = (p1.x - ray.Origin.x) * ray.DirectionInverse.x;
      float tFar = (p2.x - ray.Origin.x) * ray.DirectionInverse.x;

      if (tNear > tFar) {
         float swap = tNear;
         tNear = tFar;
         tFar = swap;
      }

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }
      state.t = minBoundNearT;
      //state.TMax = maxBoundFarT;

      tNear = (p1.y - ray.Origin.y) * ray.DirectionInverse.y;
      tFar = (p2.y - ray.Origin.y) * ray.DirectionInverse.y;
      if (tNear > tFar) {
         float swap = tNear;
         tNear = tFar;
         tFar = swap;
      }

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }

      state.t = minBoundNearT;
      //state.TMax = maxBoundFarT;

      //rayInverse = ray.DirectionInverse.z;
      tNear = (p1.z - ray.Origin.z) * ray.DirectionInverse.z;
      tFar = (p2.z - ray.Origin.z) * ray.DirectionInverse.z;
      if (tNear > tFar) {
         float swap = tNear;
         tNear = tFar;
         tFar = swap;
      }

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }

      state.t = minBoundNearT;
      //state.TMax = maxBoundFarT;

      return state;

   }

   public static Intersection GetHitInfoNew(Point3 p1, Point3 p2, Ray ray) {
      float maxBoundFarT = Float.MAX_VALUE;
      float minBoundNearT = 0;

      Intersection intersection = new Intersection();
      intersection.Hits = true;

      // x
      float tNear = (p1.x - ray.Origin.x) * ray.DirectionInverse.x;
      float tFar = (p2.x - ray.Origin.x) * ray.DirectionInverse.x;

      float swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      tFar *= 1 + 2 * FloatUtils.gamma(3);

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         intersection.Hits = false;
         return intersection;
      }

      intersection.t = minBoundNearT;
      //intersection.TMax = maxBoundFarT;

      // y
      tNear = (p1.y - ray.Origin.y) * ray.DirectionInverse.y;
      tFar = (p2.y - ray.Origin.y) * ray.DirectionInverse.y;

      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      tFar *= 1 + 2 * FloatUtils.gamma(3);

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         intersection.Hits = false;
         return intersection;
      }

      intersection.t = minBoundNearT;
      //intersection.TMax = maxBoundFarT;

      // z
      tNear = (p1.z - ray.Origin.z) * ray.DirectionInverse.z;
      tFar = (p2.z - ray.Origin.z) * ray.DirectionInverse.z;

      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      tFar *= 1 + 2 * FloatUtils.gamma(3);

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         intersection.Hits = false;
         return intersection;
      }

      intersection.t = minBoundNearT;
      //intersection.TMax = maxBoundFarT;

      return intersection;
   }

   public static List<Intersection> GetBothHitInfo(Point3 p1, Point3 p2, Ray ray) {
      float maxBoundFarT = Float.MAX_VALUE;
      float minBoundNearT = 0;

      List<Intersection> intersections = new ArrayList<>();

      // x
      float tNear = (p1.x - ray.Origin.x) * ray.DirectionInverse.x;
      float tFar = (p2.x - ray.Origin.x) * ray.DirectionInverse.x;

      float swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         return intersections;
      }

      // y
      tNear = (p1.y - ray.Origin.y) * ray.DirectionInverse.y;
      tFar = (p2.y - ray.Origin.y) * ray.DirectionInverse.y;

      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         return intersections;
      }

      // z
      tNear = (p1.z - ray.Origin.z) * ray.DirectionInverse.z;
      tFar = (p2.z - ray.Origin.z) * ray.DirectionInverse.z;

      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         return intersections;
      }

      if (minBoundNearT > 0) {
         Intersection intersection = new Intersection();
         intersection.t = minBoundNearT;
         intersection.Hits = true;
         intersections.add(intersection);
      }

      Intersection intersection = new Intersection();
      intersection.t = maxBoundFarT;
      intersection.Hits = true;
      intersections.add(intersection);

      return intersections;
   }

   public boolean Hits(Ray ray) {
      return (ray.Origin.x >= point1.x && ray.Origin.x <= point2.x
            && ray.Origin.y >= point1.y && ray.Origin.y <= point2.y
            && ray.Origin.z >= point1.z && ray.Origin.z <= point2.z) || BoundingBox.GetHitInfoOld(point1, point2, ray).Hits;
   }

   public void Translate(Vector3 vector) {
      point1.plus(vector);
      point2.plus(vector);
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof BoundingBox))
         return false;

      BoundingBox rhs = (BoundingBox) obj;
      return (point1.equals(rhs.point1) && point2.equals(rhs.point2));
   }

   public static void ExpandBoundingBox(BoundingBox box1, BoundingBox box2) {
      box1.point1.x = Math.min(box1.point1.x, box2.point1.x);
      box1.point1.y = Math.min(box1.point1.y, box2.point1.y);
      box1.point1.z = Math.min(box1.point1.z, box2.point1.z);

      box1.point2.x = Math.max(box1.point2.x, box2.point2.x);
      box1.point2.y = Math.max(box1.point2.y, box2.point2.y);


      box1.point2.z = Math.max(box1.point2.z, box2.point2.z);
   }

   public static BoundingBox Union(BoundingBox box1, BoundingBox box2) {
      float p1x = Math.min(box1.point1.x, box2.point1.x);
      float p1y = Math.min(box1.point1.y, box2.point1.y);
      float p1z = Math.min(box1.point1.z, box2.point1.z);
      Point3 p1 = new Point3(p1x, p1y, p1z);

      float p2x = Math.max(box1.point2.x, box2.point2.x);
      float p2y = Math.max(box1.point2.y, box2.point2.y);
      float p2z = Math.max(box1.point2.z, box2.point2.z);
      Point3 p2 = new Point3(p2x, p2y, p2z);

      BoundingBox box = new BoundingBox(p1, p2);
      return box;
   }

   public static BoundingBox Difference(BoundingBox b1, BoundingBox b2) {
      return new BoundingBox(b1);
   }

   public static BoundingBox Intersection(BoundingBox b1, BoundingBox b2) {
      return Union(b1, b2);
   }

   /**
    * Checks to see if b1 is strictly inside of b2.
    * @param b1
    * @param b2
    * @return
    */
   public static boolean IsInsideOf(BoundingBox b1, BoundingBox b2) {
      return (b1.point1.x > b2.point1.x
            && b1.point1.y > b2.point1.y
            && b1.point1.z > b2.point1.z
            && b1.point2.x < b2.point2.x
            && b1.point2.y < b2.point2.y
            && b1.point2.z < b2.point2.z);
   }

   public static BoundingBox GetBoundingBox(BoundingBox box, Point3 point) {
      float p1x = Math.min(box.point1.x, point.x);
      float p1y = Math.min(box.point1.y, point.y);
      float p1z = Math.min(box.point1.z, point.z);
      Point3 p1 = new Point3(p1x, p1y, p1z);

      float p2x = Math.max(box.point2.x, point.x);
      float p2y = Math.max(box.point2.y, point.y);
      float p2z = Math.max(box.point2.z, point.z);
      Point3 p2 = new Point3(p2x, p2y, p2z);

      return new BoundingBox(p1, p2);
   }
}
